package postgres

import (
	"context"
	"log"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

func (r *postgresRepository) GetShortUrl(ctx context.Context, custom_slug string) (*model.UrlModel, error) {
	log.Println("GET SHORT URL: ", custom_slug)
	query := `
		SELECT id, url, custom_slug, click_count, max_click_count
		FROM urls
		WHERE custom_slug = $1
	`

	var url model.UrlModel
	if err := r.db.GetContext(ctx, &url, query, custom_slug); err != nil {
		return nil, err
	}

	return &url, nil
}
