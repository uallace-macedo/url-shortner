package url

import (
	"github.com/gin-gonic/gin"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/services/click"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/services/url"
)

const (
	shortUrlParam = "shortUrl"
)

type urlHandler struct {
	api          *gin.Engine
	service      url.UrlService
	clickService click.ClickService
}

func NewUrlHandler(api *gin.Engine, service url.UrlService, clickService click.ClickService) *urlHandler {
	return &urlHandler{
		api:          api,
		service:      service,
		clickService: clickService,
	}
}

func (h *urlHandler) RouteList(rateLimiter gin.HandlerFunc) {
	h.api.Use(rateLimiter)
	h.api.GET("/:"+shortUrlParam, h.redirect)
}
