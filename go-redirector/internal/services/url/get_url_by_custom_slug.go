package url

import (
	"context"
	"net/http"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

func (s *urlService) GetUrlByCustomSlug(ctx context.Context, customSlug string) (*model.UrlRedirectResponse, int, error) {
	v := s.getUrlRedirectResponse(ctx, customSlug)

	if v != nil {
		return &model.UrlRedirectResponse{
			ID:  v.ID,
			Url: v.Url,
		}, http.StatusOK, nil
	}

	return nil, http.StatusNotFound, nil
}

func (s *urlService) getUrlRedirectResponse(ctx context.Context, customSlug string) *model.UrlRedirectResponse {
	r := s.getFromRedis(ctx, customSlug)
	if r != nil {
		return r
	}

	return s.getFromPostgres(ctx, customSlug)
}

func (s *urlService) getFromRedis(ctx context.Context, customSlug string) *model.UrlRedirectResponse {
	d := s.rdb.GetUrlByCustomSlug(ctx, customSlug)
	return d
}

func (s *urlService) getFromPostgres(ctx context.Context, customSlug string) *model.UrlRedirectResponse {
	v, err := s.pgr.GetUrlByCustomSlug(ctx, customSlug)
	if err != nil {
		return nil
	}

	return &model.UrlRedirectResponse{
		ID:  v.ID,
		Url: v.Url,
	}
}
