package database

import (
	"github.com/jmoiron/sqlx"
)

func ConnectPostgres(url string) (*sqlx.DB, error) {
	db, err := sqlx.Connect("postgres", url)
	if err != nil {
		return nil, err
	}

	return db, nil
}
