package models

import (
	"time"
)

type Post struct {
	Id			int			`json: "post_id" db: "post_id"`
	Title		string		`json: "post_title" db: "post_title"`
	Content		string		`json: "post_content" db: "post_content"`
	Date	time.Time	`json: "post_date" db: "post_date"`
	Update	time.Time	`json: "post_update" db: "post_update"`
}
