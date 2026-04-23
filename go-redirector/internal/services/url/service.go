package url

import (
	"context"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/repository/postgres"
)

type UrlService interface {
	GetUrlByCustomSlug(ctx context.Context, customSlug string) (*model.UrlModel, int, error)
}

type urlService struct {
	pgr postgres.PostgresRepository
}

func NewUrlService(pgr postgres.PostgresRepository) *urlService {
	return &urlService{pgr: pgr}
}
