package middleware

import (
	"context"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/go-redis/redis/v8"
)

type RateLimiter struct {
	Client *redis.Client
	Limit  int
	Window time.Duration
}

func NewRateLimiter(rL *RateLimiter) gin.HandlerFunc {
	return rL.rateLimiterMiddleware()
}

func (rL *RateLimiter) allow(ctx context.Context, key string) bool {
	pipe := rL.Client.Pipeline()

	// 1. Você guarda o COMANDO, não o resultado.
	// Nesse momento, 'incrCmd' está vazio.
	incrCmd := pipe.Incr(ctx, key)

	// 2. AGORA você envia tudo para o Redis.
	_, err := pipe.Exec(ctx)
	if err != nil && err != redis.Nil {
		return true
	}

	// 3. AGORA você lê o que o Redis respondeu.
	// O valor real (1, 2, 3...) só existe aqui.
	incr := incrCmd.Val()

	// 4. Se for o primeiro hit, precisa de um segundo comando ou
	// usar a lógica do TTL que te mandei antes.
	if incr == 1 {
		rL.Client.Expire(ctx, key, rL.Window)
	}

	return incr <= int64(rL.Limit)
}

func (rL *RateLimiter) rateLimiterMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		path := c.Request.URL.Path
		if path == "/favicon.ico" || path == "/robots.txt" {
			c.Next()
			return
		}

		key := "rl:" + c.ClientIP()
		if !rL.allow(c.Request.Context(), key) {
			c.AbortWithStatusJSON(http.StatusTooManyRequests, gin.H{
				"errors": []string{"too many requests"},
			})

			return
		}

		c.Next()
	}
}
