package url

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/services/url"
)

const (
	shortUrlParam = "shortUrl"
)

type urlHandler struct {
	api     *gin.Engine
	service url.UrlService
}

func NewUrlHandler(api *gin.Engine, service url.UrlService) *urlHandler {
	return &urlHandler{
		api:     api,
		service: service,
	}
}

func (h *urlHandler) RouteList() {
	h.api.GET("/favicon.ico", func(c *gin.Context) { c.Status(http.StatusNoContent) })
	h.api.GET("/:"+shortUrlParam, h.redirect)
}
