package main

import (
	// go standard libraries
	"net/http"
	//"strconv"
	"fmt"
	"log"
	"time"
	
	// open source libraries
	"github.com/labstack/echo/v4"
	
	// custom libraries
	"models"
)

func ServePosts (c echo.Context) error {
	var posts []models.Post
	
	rows, err := db.Query("select * from posts order by id desc")
	if err != nil {
		log.Fatal(err)
	}
	defer rows.Close()
	
	for rows.Next() {
		post := models.Post{}
		err := rows.Scan(&post.Id, &post.Author, &post.Title, &post.Content, &post.Summary, &post.Date, &post.Updated, &post.Category)
		if err != nil {
			log.Fatal(err)
		}
		// 쿼리로 받아온 Row 출력
		log.Printf("| %d\t\t| %s\t\t| %s\t\t| %s\t\t| %s\t\t| %s\t\t| %s\t\t| %s\t\t|", post.Id, post.Author, post.Title, post.Content, post.Summary, &post.Date, &post.Updated, post.Category)
		
		// posts에 새로 받아온 post append
		posts = append(posts, post)
	}
	
	fmt.Println(len(posts))
	// Render.
	return c.Render(http.StatusOK, "blog/index.html", map[string]interface{}{
		"PageTitle": "DevLee",
		"Posts": posts,
	})
}

func NewPost (c echo.Context) error {
	if (c.Request().Method == "GET") {
		return c.Render(http.StatusOK, "blog/write.html", map[string]interface{}{
			"Url": "/write",
		})
	} else if (c.Request().Method == "POST") {
		p := new(models.Post)
		if err := c.Bind(p); err != nil {
			log.Fatal(err)
			return c.String(http.StatusInternalServerError, "error")
		}
		
		t := time.Now()
		p.Date = t
		p.Updated = t
		p.Author = "이태원"
		p.Summary = p.Content
		p.Category = "Tech"
		
		_, err := db.Exec(`insert into posts values(?, ?, ?, ?, ?, ?, ?, ?)`, p.Id, p.Author, p.Title, p.Content, p.Summary, p.Date, p.Updated, p.Category)
		if err != nil {
			log.Fatal(err)
		}
		
		log.Println(p.Title)
		log.Println(p.Content)
		
		return c.Redirect(http.StatusMovedPermanently, "/")
	}
	
	return c.String(http.StatusBadRequest, "bad")
}

	/*
func DisplayPost (c echo.Context) error {

	postId := c.QueryParam("id")
	p := new(models.Post)
	
	postIdInt, err := strconv.Atoi(postId)
	if err != nil {
		return err	
	}
	
	//err = db.QueryRow("SELECT * FROM Posts WHERE post_id = ?", postIdInt).Scan(&p.Id, &p.Title, &p.Content, &p.Date, &p.Update)
	
	return c.Render(http.StatusOK, "blog/post", map[string]interface{}{
		"post": p,
	})
}
*/