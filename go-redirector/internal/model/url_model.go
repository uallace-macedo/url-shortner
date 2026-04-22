package model

type (
	UrlModel struct {
		ID            int    `db:"id"`
		Url           string `db:"url"`
		CustomSlug    string `db:"custom_slug"`
		ClickCount    int    `db:"click_count"`
		MaxClickCount int    `db:"max_click_count"`
	}
)
