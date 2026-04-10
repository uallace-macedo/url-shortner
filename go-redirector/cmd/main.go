package main

import (
	"github.com/gin-gonic/gin"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/config"
)

func main() {
	cfg := config.Load()

	r := gin.New()
	r.Run(cfg.APIPort)
}
