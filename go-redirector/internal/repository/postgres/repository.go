package postgres

import (
	"context"

	"github.com/jmoiron/sqlx"
	_ "github.com/lib/pq"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

type PostgresRepository interface {
	GetUrlByCustomSlug(ctx context.Context, customSlug string) (*model.UrlModel, error)
	RegisterClick(ctx context.Context, click *model.ClickModel) error
}

type postgresRepository struct {
	db *sqlx.DB
}

func NewPostgresRepository(db *sqlx.DB) *postgresRepository {
	return &postgresRepository{db: db}
}
