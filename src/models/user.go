package models

import (
	"time"
)

type User struct {
	Id			string		`json: "id" db: "id" gorm: "id"`
	Name		string		`json: "name" db: "name" gorm: "name"`
	Admin		int		`json: "admin" db: "admin" gorm: "admin"`		// 1: 관리자, 0: 일반 유저
	Desc		string		`json: "desc" db: "desc" gorm: "desc"`		// user describe
	SessionId	string		`json: "sessionId"`
	ExpiresAt	time.Time	`json: "expiresAt"`							// 세션 만료시간
}

func (u *User) Valid() bool {
	// Check user's session is valid
	return u.ExpiresAt.Sub(time.Now()) > 0 
}

func (u *User) Refresh() time.Time {
	// Session 30분 연장
	u.ExpiresAt = u.ExpiresAt.Add(time.Minute * 30)
	
	return u.ExpiresAt
}