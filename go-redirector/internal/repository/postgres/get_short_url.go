package postgres

import (
	"context"
	"log"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

func (r *postgresRepository) GetShortUrl(ctx context.Context, shortUrl string) (*model.UrlModel, error) {
	log.Println("GET SHORT URL: ", shortUrl)
	query := `
		SELECT id, url, short_url
		FROM urls
		WHERE short_url = $1
	`

	var url model.UrlModel
	if err := r.db.GetContext(ctx, &url, query, shortUrl); err != nil {
		return nil, err
	}

	return &url, nil
}
