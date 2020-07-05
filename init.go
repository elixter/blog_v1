package main

import (
	// standard go libraries
	"database/sql"
	
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"github.com/labstack/echo-contrib/session"
	"github.com/gorilla/sessions"
	//"github.com/gorilla/securecookie"
)

var db *sql.DB
var dbErr error
var store *sessions.CookieStore

func NewEcho() *echo.Echo {
	
	
	e := echo.New()
	
	e.Renderer = NewTemplate()		// Set template renderer
	
	// 미들웨어 체인 순서 잘 생각해서 정리할 것.
	
	// session store
	//authKeyOne := securecookie.GenerateRandomKey(64)
	//encryptionKeyOne := securecookie.GenerateRandomKey(32)

	store = sessions.NewCookieStore([]byte("secret"))
	
	// Add middlewares
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	e.Use(session.Middleware(store))
	
	// Static config
	e.Use(middleware.StaticWithConfig(middleware.StaticConfig{
		Root: "statics",
	}))
	
	return e
}