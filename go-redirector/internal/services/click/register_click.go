package click

import (
	"strings"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/model"
)

func (s *clickService) RegisterClick(urlId int64, c *gin.Context) {
	click := &model.ClickModel{
		UrlId:     urlId,
		ClickedAt: time.Now().UTC(),
		IpAddress: anonymizeIp(c.ClientIP()),
		Referrer:  c.Request.Referer(),
	}

	s.pgr.RegisterClick(c, click)
}

func anonymizeIp(ip string) string {
	parts := strings.Split(ip, ".")
	if len(parts) == 4 {
		parts[3] = "0"
		return strings.Join(parts, ".")
	}

	return ip
}
