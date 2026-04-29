package model

import (
	"database/sql"
	"time"
)

type (
	UrlModel struct {
		ID            int64         `db:"id"`
		Url           string        `db:"url"`
		CustomSlug    string        `db:"custom_slug"`
		ClickCount    int64         `db:"click_count"`
		MaxClickCount sql.NullInt64 `db:"max_click_count"`
		ExpiresAt     *time.Time    `db:"expires_at"`
		Active        bool          `db:"active"`
	}

	UrlRedirectResponse struct {
		ID            int64
		Url           string
		ClickCount    int64
		MaxClickCount sql.NullInt64
		ExpiresAt     *time.Time
		Active        bool
	}
)

func (u *UrlRedirectResponse) CanAccess() bool {
	canClick := !u.MaxClickCount.Valid || u.MaxClickCount.Int64 > int64(u.ClickCount)
	notExpired := u.ExpiresAt == nil || u.ExpiresAt.After(time.Now().UTC())

	return canClick && notExpired && u.Active
}
