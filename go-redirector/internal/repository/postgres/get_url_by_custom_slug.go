package postgres

import (
	"context"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

func (r *postgresRepository) GetUrlByCustomSlug(ctx context.Context, customSlug string) (*model.UrlModel, error) {
	query := `
		SELECT id, url, custom_slug, click_count, max_click_count
		FROM urls
		WHERE custom_slug = $1
	`

	var url model.UrlModel
	if err := r.db.GetContext(ctx, &url, query, customSlug); err != nil {
		return nil, err
	}

	return &url, nil
}
