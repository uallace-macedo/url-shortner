package url

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"
)

func (s *urlService) GetOriginalUrl(ctx context.Context, short_url string) (string, int, error) {
	v, err := s.pgr.GetShortUrl(ctx, short_url)
	if err != nil {
		if err == sql.ErrNoRows {
			return "", http.StatusNotFound, fmt.Errorf("url not found")
		}

		return "", http.StatusInternalServerError, fmt.Errorf("an unexpected error occurred")
	}

	return v.Url, http.StatusOK, nil
}
