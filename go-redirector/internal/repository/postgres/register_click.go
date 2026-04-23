package postgres

import (
	"context"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

func (r *postgresRepository) RegisterClick(ctx context.Context, click *model.ClickModel) error {
	query := `
		INSERT INTO clicks (clicked_at, country, ip_address, referrer, url_id)
		VALUES (:clicked_at, :country, :ip_address, :referrer, :url_id)
	`

	_, err := r.db.NamedExecContext(ctx, query, click)
	return err
}
