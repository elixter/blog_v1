package models

import (
	"time"
)

type Post struct {
	Id			int			`json: "id" db: "id" gorm: "id"`
	Author		string		`json: "author" db: "author" gorm: "author"`
	Title		string		`json: "title" db: "title" gorm: "title"`
	Content		string		`json: "content" db: "content" gorm: "content"`
	Summary		string		`json: "summary" db: "summary" gorm: "summary"`
	Date	time.Time	`json: "date" db: "date" gorm: "date"`
	Updated	time.Time	`json: "updated" db: "update" gorm: "updated"`
	Category string `json: category" db: "category" gorm: "category"`
}


type Comment struct {
	Id			int		`json: "id" db: "id" gorm: "id"`
	Content			string			`json: "content" db: "content" json: "content"`
	Author			string			`json: "author" db: "author" gorm: "author"`
	Date		time.Time		`json: "date" db: "date" gorm: "date"`
	Updated			time.Time		`json: "updated" db: "updated" gorm: "updated"`
	Pid			int			`json: "pid" db: "pid" gorm: "pid"`
}

type User struct {
	Id			string		`json: "id" db: "id" gorm: "id"`
	Name		string		`json: "name" db: "name" gorm: "name"`
	Admin		bool		`json: "admin" db: "admin" gorm: "admin"`
	desc		string		`json: "desc" db: "desc" gorm: "desc"`		// user describe
}