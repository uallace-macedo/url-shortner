package url

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

func (h *urlHandler) redirect(c *gin.Context) {
	shortUrl := c.Param(shortUrlParam)
	v, code, err := h.service.GetOriginalUrl(c, shortUrl)

	if err != nil {
		c.AbortWithStatusJSON(code, gin.H{"error": "URL not found"})
		return
	}

	c.Redirect(http.StatusTemporaryRedirect, v)
}
