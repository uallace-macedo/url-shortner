package url

import (
	"context"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/repository/postgres"
)

type UrlService interface {
	GetOriginalUrl(ctx context.Context, short_url string) (string, int, error)
}

type urlService struct {
	pgr postgres.PostgresRepository
}

func NewUrlService(pgr postgres.PostgresRepository) *urlService {
	return &urlService{pgr: pgr}
}
