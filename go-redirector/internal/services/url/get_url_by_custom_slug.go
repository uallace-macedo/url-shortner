package url

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

func (s *urlService) GetUrlByCustomSlug(ctx context.Context, customSlug string) (*model.UrlModel, int, error) {
	v, err := s.pgr.GetUrlByCustomSlug(ctx, customSlug)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, http.StatusNotFound, fmt.Errorf("url not found")
		}

		return nil, http.StatusInternalServerError, fmt.Errorf("an unexpected error occurred")
	}

	return v, http.StatusOK, nil
}
