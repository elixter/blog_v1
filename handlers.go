package main

import (
	// go standard libraries
	"net/http"
	"strconv"
	"fmt"
	"log"
	"time"
	"strings"
	
	// open source libraries
	"github.com/labstack/echo/v4"
	"github.com/grokify/html-strip-tags-go"
	
	// custom libraries
	"models"
)


// 블로그 첫 화면에 쓰이는 데이터 serving
func ServePosts (c echo.Context) error {
	var posts []models.Post
	
	Rows, err := db.Query("select * from posts order by id desc")
	if err != nil {
		log.Fatal(err)
	}
	defer Rows.Close()
	
	for Rows.Next() {
		p := models.Post{}
		err := Rows.Scan(&p.Id, &p.Author, &p.UDesc, &p.Title, &p.Content, &p.Summary, &p.Date, &p.Updated, &p.Category)
		if err != nil {
			return err;
		}

		// 쿼리로 받아온 Row 출력
		log.Printf("| %d\t\t| %s\t\t| %s\t\t| %s\t\t| %s\t\t| %s\t\t| %s\t\t| %s\t\t| %s\t\t|", p.Id, p.Author, p.UDesc, p.Title, p.Content, p.Summary, &p.Date, &p.Updated, p.Category)
		
		// posts에 새로 받아온 post append
		posts = append(posts, p)
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

// 글쓰기 화면
func NewPost (c echo.Context) error {
	if (c.Request().Method == "GET") {
		// Request method가 GET인 경우
		return c.Render(http.StatusOK, "blog/write.html", map[string]interface{}{
			"Url": "/write",
			"CancelUrl": "/",
		})
	} else if (c.Request().Method == "POST") {
		// Request method가 POST인 경우
		p := new(models.Post)
		if err := c.Bind(p); err != nil {
			log.Fatal(err)
			return c.String(http.StatusInternalServerError, "error")
		}
		
		var sumText string				// Summary text
		
		p.Author = "이태원"
		t := time.Now()
		p.Date = t
		p.Updated = t
		
		// Summary 만들기
		if (len(p.Content) >= 200) {
			if (strings.Contains(p.Content, "&nbsp;")) {
				sumText = strings.Split(p.Content, "&nbsp;")[0]
			} else {
				sumText = p.Content[:200]
			}
		}  else {
			if (strings.Contains(p.Content, "&nbsp;")) {
				sumText = strings.Split(p.Content, "&nbsp;")[0]
			} else {
				sumText = p.Content
			}
		}
		p.Summary = strip.StripTags(sumText)
		p.Category = "Tech"
		
		
		// DB insert query
		_, err := db.Exec(`insert into posts values(?, ?, ?, ?, ?, ?, ?, ?, ?)`, p.Id, p.Author, "임시", p.Title, p.Content, p.Summary, p.Date, p.Updated, p.Category)
		if err != nil {
			log.Fatal(err)
		}
		
		log.Println(p.Title)
		log.Println(p.Content)
		
		return c.Redirect(http.StatusMovedPermanently, "/")
	}
	
	return c.String(http.StatusBadRequest, "bad")
}

// 게시글 view
func ServePost (c echo.Context) error {

	pid := c.QueryParam("id")
	p := new(models.Post)
	
	intPid, err := strconv.Atoi(pid)
	if err != nil {
		return c.String(http.StatusInternalServerError, "게시글이 존재하지 않습니다.")	
	}
	
	err = db.QueryRow("SELECT * FROM posts WHERE id = ?", intPid).Scan(&p.Id, &p.Author, &p.UDesc, &p.Title, &p.Content, &p.Summary, &p.Date, &p.Updated, &p.Category)

	
	return c.Render(http.StatusOK, "blog/post.html", map[string]interface{}{
		"PageTitle": "DevLee",
		"Post": p,
		"Admin": 1,
		"Id": p.Id,
		"EditUrl": "edit",
		"DeleteUrl": "delete",
		"Modify": 0,
	})
}


// 게시글 삭제
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

// 게시글 수정
func EditPost (c echo.Context) error {
	p := new(models.Post)		// 수정할 게시글 object
	pid := c.QueryParam("id")
	
	intPid, err := strconv.Atoi(pid)
	if err != nil {
		return c.String(http.StatusInternalServerError, "id 정수형 변환 실패")	
	}

	if (c.Request().Method == "GET") {	
		// Request method가 GET인 경우
		

		err = db.QueryRow("SELECT title, content FROM posts WHERE id = ?", intPid).Scan(&p.Title, &p.Content)
		
		
		return c.Render(http.StatusOK, "blog/write.html", map[string]interface{}{
			"Url": "/edit",
			"Id": pid,
			"Modify": 1,
			"EditPost": p,
			"CancelUrl:": "/",
		})
	} else if (c.Request().Method == "POST") {
		// Request method가 POST인 경우

		if err := c.Bind(p); err != nil {
			log.Fatal(err)
			return c.String(http.StatusInternalServerError, "error")
		}
		
		t := time.Now()
		p.Updated = t
		
		// DB Update
		_, err := db.Exec("update posts set title = ?, content = ?, updated = ? where id = ?", p.Title, p.Content, p.Updated, intPid)
		if err != nil {
			log.Fatal(err)
		}
		redirectUrl := "/post?id=" + pid
		
		log.Println(redirectUrl)
		
		return c.Redirect(http.StatusMovedPermanently, redirectUrl)
	}
	
	return c.String(http.StatusBadRequest, "bad")
}
