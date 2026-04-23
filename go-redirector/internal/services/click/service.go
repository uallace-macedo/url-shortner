package click

import (
	"github.com/gin-gonic/gin"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/repository/postgres"
)

type (
	ClickService interface {
		RegisterClick(urlId int64, c *gin.Context)
	}

	clickService struct {
		pgr postgres.PostgresRepository
	}
)

func NewClickService(pgr postgres.PostgresRepository) *clickService {
	return &clickService{pgr: pgr}
}
