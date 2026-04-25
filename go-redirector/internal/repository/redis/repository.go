package redis

import (
	"context"

	"github.com/go-redis/redis/v8"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

type (
	RedisRepository interface {
		GetUrlByCustomSlug(ctx context.Context, customSlug string) *model.UrlRedirectResponse
	}

	redisRepository struct {
		rc *redis.Client
	}
)

func NewRedisRepository(rc *redis.Client) *redisRepository {
	return &redisRepository{rc: rc}
}
