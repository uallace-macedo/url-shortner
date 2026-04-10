package model

type (
	UrlModel struct {
		ID       int    `db:"id"`
		Url      string `db:"url"`
		ShortURL string `db:"short_url"`
	}
)
