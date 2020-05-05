package main

import (
	// standard go libraries
	//"database/sql"
	
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

func NewEcho() *echo.Echo {
	e := echo.New()
	
	e.Renderer = NewTemplate()		// Set template renderer
	
	// Regist middlewares
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	
	return e
}