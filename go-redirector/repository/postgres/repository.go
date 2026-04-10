package postgres

import (
	"context"

	"github.com/jmoiron/sqlx"
	_ "github.com/lib/pq"
	"github.com/uallace-macedo/url-shortner/go-redirector/model"
)

type PostgresRepository interface {
	GetShortUrl(context context.Context, shortUrl string) (*model.UrlModel, error)
}

type postgresRepository struct {
	db *sqlx.DB
}

func NewPostgresRepository(db *sqlx.DB) *postgresRepository {
	return &postgresRepository{db: db}
}
