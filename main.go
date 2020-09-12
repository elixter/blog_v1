package main

import (
	"database/sql"
	// defualt libraries.
	//"net/http"
	"log"

	// open source libraries.
	_ "github.com/go-sql-driver/mysql"

	"controllers"
)

func main() {
	// open db
	db, dbErr = sql.Open("mysql", "root:@tcp(127.0.0.1:3306)/blog?parseTime=true&charset=utf8mb4")
	if dbErr != nil {
		log.Fatal(dbErr)
	}
	defer db.Close()
	
	e := NewEcho()
	
	blog := e.Group("/blog")
	//blog.Use(SessionHandler)
	
	// Default Routing
	// GET Routing
	e.GET("/", Index)
	e.GET("/login", Login)
	e.GET("/logout", Logout)
	e.GET("/project", NotFound)
	e.GET("/about", NotFound)
	
	//POST Routing
	e.POST("/login", Login)
	e.POST("/logout", Logout)
	
	// BLOG API Routing
	// GET Routing
	blog.GET("", ServePosts)
	blog.GET("/:condition/:keyword", ConditianalServePosts)
	blog.GET("/post", ServePost)
	blog.GET("/post/:id", ServePost)
	blog.GET("/write", NewPost, AuthHandler)
	blog.GET("/edit/:id", EditPost, AuthHandler)
	blog.GET("/search", ConditianalServePosts)
	blog.GET("/login", Login)
	blog.GET("/logout", Logout)
	
	// POST Routing
	blog.POST("", ServePosts)
	blog.POST("/write", NewPost)
	blog.POST("/edit/:id", EditPost)
	blog.POST("/fileUpload", controllers.CKUpload)
	blog.POST("/login", Login)
	blog.POST("/logout", Logout)
	blog.POST("/delete/:id", DeletePost, AuthHandler)
	
	// Run server with port 8080
	e.Logger.Fatal(e.Start(":8080"))
}