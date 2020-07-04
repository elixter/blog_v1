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
	Admin		int		`json: "admin" db: "admin" gorm: "admin"`		// 1: 관리자, 0: 일반 유저
	Desc		string		`json: "desc" db: "desc" gorm: "desc"`		// user describe
}

type Session struct {
	Id			string			`json: "id" db: "id" gorm: "id"`
	Uid			string			`json: "uid" db: "uid" gorm: "uid"`
	CreatedAt		time.Time		`json: "createdAt" db: "createdAt" gorm: "createdAt"`
	ExpiresAt		time.Time		`json: "expiresAt" db: "expiresAt" gorm: "expiresAt"`
}