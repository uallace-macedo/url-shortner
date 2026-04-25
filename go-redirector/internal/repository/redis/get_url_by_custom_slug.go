package redis

import (
	"context"
	"strconv"

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

	id, _ := strconv.Atoi(data["id"])
	url := data["url"]

	return &model.UrlRedirectResponse{
		ID:  int64(id),
		Url: url,
	}
}
