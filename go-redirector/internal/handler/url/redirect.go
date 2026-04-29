package url

import (
	"net/http"
	"strings"
	"time"

	"github.com/gin-gonic/gin"
)

func (h *urlHandler) redirect(c *gin.Context) {
	shortUrl := c.Param(shortUrlParam)
	v, code, err := h.service.GetUrlByCustomSlug(c, shortUrl)

	if err != nil || v == nil {
		c.AbortWithStatusJSON(code, gin.H{
			"timestamp": time.Now().UTC(),
			"error":     "URL not found",
		})
		return
	}

	if !v.CanAccess() {
		c.AbortWithStatusJSON(http.StatusGone, gin.H{
			"timestamp": time.Now().UTC(),
			"error":     "URL not accessible",
		})
		return
	}

	if isBot(c.Request.UserAgent()) {
		c.Redirect(http.StatusFound, v.Url)
		return
	}

	c.Redirect(http.StatusFound, v.Url)
	go h.clickService.RegisterClick(v.ID, c)
}

func isBot(userAgent string) bool {
	ua := strings.ToLower(userAgent)
	bots := []string{
		"bot", "crawler", "spider", "slurp",
		"facebookexternalhit", "twitterbot",
		"googlebot", "bingbot", "curl", "wget",
	}

	for _, bot := range bots {
		if strings.Contains(ua, bot) {
			return true
		}
	}

	return false
}
