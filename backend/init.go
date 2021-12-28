package main

import (
	"blog/service"
	// standard go libraries
	"database/sql"
	"fmt"
	"log"
	"os"

	"github.com/gorilla/sessions"
	"github.com/labstack/echo-contrib/session"
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	//"github.com/gorilla/securecookie"
)

const (
	authCookieStoreName = "auth"
)

func InitService() service.Service {
	e := echo.New()

	e.Renderer = NewTemplate() // Set template renderer

	// 미들웨어 체인 순서 잘 생각해서 정리할 것.

	// session store
	//authKeyOne := securecookie.GenerateRandomKey(64)
	//encryptionKeyOne := securecookie.GenerateRandomKey(32)

	store := sessions.NewCookieStore([]byte(authCookieStoreName))
	store.Options = &sessions.Options{
		Path:     "/",
		MaxAge:   3600,
		HttpOnly: true,
	}

	// Add middlewares
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	e.Use(middleware.Secure())
	e.Use(session.Middleware(store))

	dbIp := os.Getenv("DB_IP")
	dbPort := os.Getenv("DB_PORT")
	dbId := os.Getenv("DB_ID")
	dbPw := os.Getenv("DB_PW")

	dbSource := fmt.Sprintf("%s:%s@tcp(%s:%s)/blog2?parseTime=true&charset=utf8mb4", dbId, dbPw, dbIp, dbPort)

	db, err := sql.Open("mysql", dbSource)
	if err != nil {
		log.Fatal(err)
	}

	r := service.Service{
		EchoRouter: e,
		DB: db,
		SessionStore: store,
	}

	return r
}
