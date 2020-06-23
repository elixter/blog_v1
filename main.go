package main

import (
	// defualt libraries.
	//"net/http"
	"log"
	"database/sql"
	
	
	// open source libraries.
	_ "github.com/go-sql-driver/mysql"
	
)

func main() {
	// open db
	db, dbErr = sql.Open("mysql", "root:@tcp(127.0.0.1:3306)/blog?parseTime=true&charset=utf8mb4")
	if dbErr != nil {
		log.Fatal(dbErr)
	}
	defer db.Close()
	
	e := NewEcho()
	
	// GET Actions
	e.GET("/", ServePosts)
	e.GET("/write", NewPost)
	e.GET("/post", ServePost)
	e.GET("/delete", DeletePost)
	
	
	// Post Actions
	e.POST("/write", NewPost)
	
	
	
	// Run server with port 8080
	e.Logger.Fatal(e.Start(":8080"))
}