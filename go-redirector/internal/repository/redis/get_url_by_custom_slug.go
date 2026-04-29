package redis

import (
	"context"
	"database/sql"
	"strconv"
	"time"

	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

func (r *redisRepository) GetUrlByCustomSlug(ctx context.Context, customSlug string) *model.UrlRedirectResponse {
	data, err := r.rc.HGetAll(ctx, customSlug).Result()
	if err != nil {
		return nil
	}

	if len(data) == 0 {
		return nil
	}

	u := MapToModel(data)
	return u
}

func MapToModel(data map[string]string) *model.UrlRedirectResponse {
	u := &model.UrlRedirectResponse{}

	id, _ := strconv.ParseInt(data["id"], 10, 64)
	u.ID = id

	cc, _ := strconv.ParseInt(data["click_count"], 10, 64)
	u.ClickCount = int64(cc)

	if val, ok := data["max_click_count"]; ok && val != "" && val != "null" {
		parsed, err := strconv.ParseInt(val, 10, 64)
		if err == nil {
			u.MaxClickCount = sql.NullInt64{Int64: parsed, Valid: true}
		}
	} else {
		u.MaxClickCount = sql.NullInt64{Valid: false}
	}

	if val, ok := data["expires_at"]; ok && val != "" && val != "null" {
		t, err := time.Parse(time.RFC3339, val)
		if err == nil {
			u.ExpiresAt = &t
		}
	}

	u.Active = data["active"] == "true"

	return u
}
