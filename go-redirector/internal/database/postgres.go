package database

import (
	"github.com/jmoiron/sqlx"
)

func ConnectPostgres(url string) (*sqlx.DB, error) {
	db, err := sqlx.Connect("postgres", url)
	return db, err
}
