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
	count, _ := rL.Client.Incr(ctx, key).Result()

	if count == 1 {
		rL.Client.Expire(ctx, key, rL.Window)
	}

	return count <= int64(rL.Limit)
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
