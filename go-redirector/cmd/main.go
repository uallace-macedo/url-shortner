package main

import (
	"fmt"

	"github.com/gin-gonic/gin"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/config"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/database"
	urlHandl "github.com/uallace-macedo/url-shortner/go-redirector/internal/handler/url"
	pgRepo "github.com/uallace-macedo/url-shortner/go-redirector/internal/repository/postgres"
	urlServ "github.com/uallace-macedo/url-shortner/go-redirector/internal/services/url"
)

func main() {
	lgg := config.NewLogger("")
	cfg := config.Load()

	pgUrl := fmt.Sprintf(
		"postgres://%s:%s@%s:%s/%s?sslmode=disable",
		cfg.PSQLUser, cfg.PSQLPassword, cfg.PSQLHost, cfg.PSQLPort, cfg.PSQLDatabase,
	)

	pgDb, err := database.ConnectPostgres(pgUrl)
	if err != nil {
		lgg.Errorf("could not connect to db; %v", err)
	}

	lgg.Info("pg db connected")

	pgRepo := pgRepo.NewPostgresRepository(pgDb)
	urlServ := urlServ.NewUrlService(pgRepo)

	r := gin.New()
	urlHandl := urlHandl.NewUrlHandler(r, urlServ)
	urlHandl.RouteList()

	r.Run(cfg.APIPort)
}
