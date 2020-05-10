package main

import (
	// go standard libraries
	"net/http"
	
	// open source libraries
	"github.com/labstack/echo/v4"
	
	// custom libraries
	"models"
)

func DisplayPosts (c echo.Context) error {
	var posts []models.Post
	
	// Query posts from database.
	schema := `SELECT post_title, post_date FROM Posts order by post_date desc;`
	rows, err := db.Query(schema)
	if err != nil {
		panic(err)
	}
	defer rows.Close()
	
	// Save data to Post container.
	for rows.Next() {
		post := models.Post{}
		if err := rows.Scan(&post.Title, &post.Date); err != nil {
			// if there is something wrong, return err.
			return err
		}
		posts = append(posts, post)
	}
	
	// Render.
	return c.Render(http.StatusOK, "blog/welcome.html", map[string]interface{}{
		"Posts": posts,
	})
}