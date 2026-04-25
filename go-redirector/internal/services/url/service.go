package url

import (
	"context"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/repository/postgres"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/repository/redis"
)

type UrlService interface {
	GetUrlByCustomSlug(ctx context.Context, customSlug string) (*model.UrlRedirectResponse, int, error)
}

type urlService struct {
	pgr postgres.PostgresRepository
	rdb redis.RedisRepository
}

func NewUrlService(pgr postgres.PostgresRepository, rdb redis.RedisRepository) *urlService {
	return &urlService{
		pgr: pgr,
		rdb: rdb,
	}
}
