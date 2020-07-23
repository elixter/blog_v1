package models

import (
	"time"
)

type Post struct {
	Id			int			`json: "id" db: "id" gorm: "id"`
	Author		string		`json: "author" db: "author" gorm: "author"`
	UDesc		string		`json: "udesc" db: "udesc" gorm: "udesc"`
	Title		string		`json: "title" db: "title" gorm: "title"`
	Content		string		`json: "content" db: "content" gorm: "content"`
	Summary		string		`json: "summary" db: "summary" gorm: "summary"`
	Date	time.Time	`json: "date" db: "date" gorm: "date"`
	Updated	time.Time	`json: "updated" db: "update" gorm: "updated"`
	Category string `json: category" db: "category" gorm: "category"`
	HashTags []string
}