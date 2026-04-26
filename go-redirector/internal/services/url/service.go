package url

import (
	"context"

	"github.com/go-redis/redis/v8"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/repository/postgres"
	redisRepo "github.com/uallace-macedo/url-shortner/go-redirector/internal/repository/redis"
)

type UrlService interface {
	GetUrlByCustomSlug(ctx context.Context, customSlug string) (*model.UrlRedirectResponse, int, error)
}

type urlService struct {
	pgr     postgres.PostgresRepository
	rdb     redisRepo.RedisRepository
	rClient *redis.Client
}

func NewUrlService(pgr postgres.PostgresRepository, rdb redisRepo.RedisRepository, rClient *redis.Client) *urlService {
	return &urlService{
		pgr:     pgr,
		rdb:     rdb,
		rClient: rClient,
	}
}
