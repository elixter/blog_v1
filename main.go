package main

import (
	// defualt libraries.
	"net/http"
	"database/sql"
	
	// open source libraries.
	"github.com/labstack/echo/v4"
	
	_ "github.com/go-sql-driver/mysql"
)

var db *sql.DB
var dbErr error

func main() {
	e := NewEcho()
	
	// Database open
	db, dbErr = sql.Open("mysql", "root:@tcp(127.0.0.1:3306)/blog?parseTime=true&charset=utf8mb4")
	if dbErr != nil {
		panic(dbErr)
	}
	defer db.Close()
	
	e.GET("/", func(c echo.Context) error {
		return c.Render(http.StatusOK, "blog/index.html", map[string]interface{}{})
	})
	
	// Post routing
	e.GET("/blog", DisplayPosts)
		
	// Run server with port 8080
	e.Logger.Fatal(e.Start(":8080"))
}