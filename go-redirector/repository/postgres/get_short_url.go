package postgres

import (
	"context"

	"github.com/uallace-macedo/url-shortner/go-redirector/model"
)

func (r *postgresRepository) GetShortUrl(ctx context.Context, shortUrl string) (*model.UrlModel, error) {
	query := `
		SELECT id, url, short_url
		FROM urls
		WHERE short_url = ?
	`

	var url model.UrlModel
	if err := r.db.GetContext(ctx, &url, query, shortUrl); err != nil {
		return nil, err
	}

	return &url, nil
}
