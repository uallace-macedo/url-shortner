package url

import (
	"context"
	"net/http"
	"strconv"
	"time"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

func (s *urlService) GetUrlByCustomSlug(ctx context.Context, customSlug string) (*model.UrlRedirectResponse, int, error) {
	v := s.getUrlRedirectResponse(ctx, customSlug)

	if v == nil {
		return nil, http.StatusNotFound, nil
	}

	s.manageHotCache(ctx, customSlug, v)
	return v, http.StatusOK, nil
}

func (s *urlService) manageHotCache(ctx context.Context, customSlug string, resp *model.UrlRedirectResponse) {
	count, _ := s.rClient.Exists(ctx, customSlug).Result()
	redis := s.rClient

	if count == 0 {
		data := map[string]string{
			"id":          strconv.FormatInt(resp.ID, 10),
			"url":         resp.Url,
			"click_count": strconv.FormatInt(resp.ClickCount, 10),
			"active":      strconv.FormatBool(resp.Active),
		}

		if resp.MaxClickCount.Valid {
			data["max_click_count"] = strconv.FormatInt(resp.MaxClickCount.Int64, 10)
		} else {
			data["max_click_count"] = "null"
		}

		if resp.ExpiresAt != nil {
			data["expires_at"] = resp.ExpiresAt.Format(time.RFC3339)
		} else {
			data["expires_at"] = "null"
		}

		redis.HSet(ctx, customSlug, data)
		redis.Expire(ctx, customSlug, 40*time.Second)
	}

	// hit key
	hitKey := "hit:" + customSlug
	count, _ = redis.Incr(ctx, hitKey).Result()

	if count == 1 {
		redis.Expire(ctx, hitKey, 30*time.Second)
	}

	if count >= 5 {
		redis.Del(ctx, hitKey)
		redis.Expire(ctx, customSlug, 40*time.Second)
	}
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
		ID:            v.ID,
		Url:           v.Url,
		ClickCount:    v.ClickCount,
		MaxClickCount: v.MaxClickCount,
		ExpiresAt:     v.ExpiresAt,
		Active:        v.Active,
	}
}
