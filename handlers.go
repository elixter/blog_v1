package main

import (
	// go standard libraries
	"net/http"
	"strconv"
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
		"Admin": 1,
		"WriteUrl": "/write",
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
		
		// Content내용 정제시켜서 Summary에 넣는작업 해야됨.
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

func ServePost (c echo.Context) error {

	pid := c.QueryParam("id")
	p := new(models.Post)
	
	intPid, err := strconv.Atoi(pid)
	if err != nil {
		return err	
	}
	
	err = db.QueryRow("SELECT * FROM posts WHERE id = ?", intPid).Scan(&p.Id, &p.Author, &p.Title, &p.Content, &p.Summary, &p.Date, &p.Updated, &p.Category)
	
	return c.Render(http.StatusOK, "blog/post.html", map[string]interface{}{
		"PageTitle": "DevLee",
		"Post": p,
		"Admin": 1,
		"Id": p.Id,
		"EditUrl": "edit",
		"DeleteUrl": "delete",
	})
}

func DeletePost (c echo.Context) error {
	id := c.QueryParam("id")
	
	intId, sconvErr := strconv.Atoi(id)
	if sconvErr != nil {
		log.Fatal(sconvErr)
	}
	
	_, err := db.Exec("delete from posts where id = ?", intId)
	if err != nil {
		log.Fatal(err)
	}
	
	// auto_increment initialize and sort.
	_, err = db.Exec("ALTER TABLE posts AUTO_INCREMENT=1;")
	if err != nil {
		return err
	}
	
	_, err = db.Exec("SET @COUNT = 0;")
	if err != nil {
		return err
	}

	_, err = db.Exec("UPDATE posts SET id = @COUNT := @COUNT+1;")
	if err != nil {
		return err
	}
	
	return c.Redirect(http.StatusMovedPermanently, "/")
}
