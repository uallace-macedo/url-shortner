package main

import (
	"time"

	"github.com/gin-gonic/gin"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/config"
	urlHandl "github.com/uallace-macedo/url-shortner/go-redirector/internal/handler/url"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/middleware"
	pgRepo "github.com/uallace-macedo/url-shortner/go-redirector/internal/repository/postgres"
	rdRepo "github.com/uallace-macedo/url-shortner/go-redirector/internal/repository/redis"
	clickServ "github.com/uallace-macedo/url-shortner/go-redirector/internal/services/click"
	urlServ "github.com/uallace-macedo/url-shortner/go-redirector/internal/services/url"
)

func main() {
	logger := config.NewLogger("")
	cfg := config.Load()

	pgDb, rDb := cfg.GetDatabases(*logger)

	pgRepo := pgRepo.NewPostgresRepository(pgDb)
	rdRepo := rdRepo.NewRedisRepository(rDb)

	urlServ := urlServ.NewUrlService(pgRepo, rdRepo, rDb)
	clickServ := clickServ.NewClickService(pgRepo)

	rateLimiter := middleware.NewRateLimiter(
		&middleware.RateLimiter{
			Client: rDb,
			Limit:  3,
			Window: 10 * time.Second,
		},
	)

	gin.SetMode(gin.DebugMode)
	r := gin.Default()

	urlHandl := urlHandl.NewUrlHandler(r, urlServ, clickServ)
	urlHandl.RouteList(rateLimiter)

	r.Run(cfg.APIPort)
}
