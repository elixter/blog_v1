package main

import (
	// standard go libraries
	"database/sql"
	
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

var db *sql.DB
var dbErr error

func NewEcho() *echo.Echo {
	
	
	e := echo.New()
	
	e.Renderer = NewTemplate()		// Set template renderer
	
	// 미들웨어 체인 순서 잘 생각해서 정리할 것.
	
	// Add middlewares
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	
	// Static config
	e.Use(middleware.StaticWithConfig(middleware.StaticConfig{
		Root: "statics",
	}))
	
	return e
}