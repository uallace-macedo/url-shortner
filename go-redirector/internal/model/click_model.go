package model

import "time"

type (
	ClickModel struct {
		ID        int64     `db:"id"`
		ClickedAt time.Time `db:"clicked_at"`
		Country   string    `db:"country"`
		IpAddress string    `db:"ip_address"`
		Referrer  string    `db:"referrer"`
		UrlId     int64     `db:"url_id"`
	}
)
