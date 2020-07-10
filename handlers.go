package main

import (
	// go standard libraries
	"net/http"
	"strconv"
	"fmt"
	"log"
	"time"
	"strings"
	//"encoding/json"
	
	// open source libraries
	"github.com/labstack/echo/v4"
	"github.com/grokify/html-strip-tags-go"
	"github.com/labstack/echo-contrib/session"
	
	// custom libraries
	"models"
)


// 블로그 첫 화면에 쓰이는 데이터 serving
func ServePosts (c echo.Context) error {
	var posts []models.Post
	var isAdmin int
	
	u := new(models.User)
	
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
	
	// 세션에서 현재 유저 정보 가져오기
	sess, err := session.Get("session", c)
	if err != nil {
		log.Println(err)
	}
	
	// 로그인정보가 존재하는 경우
	if (len(sess.Values) > 1) {
		_, err = u.GetUser(sess)
		if err != nil {
			log.Println(err)
		}
		
		// Database에 유저 세션정보가 있는지 확인
		if (u.Check(db, "ID")) {
			isAdmin = u.Admin	
		} else {
			isAdmin = 0
		}
	} else {
		isAdmin = 0
	}
	
	fmt.Println(len(posts))
	// Render.
	return c.Render(http.StatusOK, "blog/index.html", map[string]interface{}{
		"PageTitle": "DevLee",
		"Posts": posts,
		"Admin": isAdmin,
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
		u := new(models.User)
		
		// 세션에서 현재 유저 정보 가져오기
		sess, err := session.Get("session", c)
		if err != nil {
			log.Println(err)
		}
		
		u, err = u.GetUser(sess)
		if err != nil {
			log.Println(err)
		}
		
		// Form에서 작성한 게시글 데이터 받아오기
		if err := c.Bind(p); err != nil {
			log.Fatal(err)
			return c.String(http.StatusInternalServerError, "error")
		}
		
		var sumText string	// Summary text
		
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
		
		t := time.Now() // 현재시간 가져오기
		
		// Post객체에 각 데이터 넣기
		p.Author = u.Name
		p.UDesc = u.Desc 
		p.Date = t
		p.Updated = t
		p.Summary = strip.StripTags(sumText)
		p.Category = "Tech"		// 추후 설정하도록 만들 예정
		
		
		// DB insert query
		_, err = db.Exec(`insert into posts values(?, ?, ?, ?, ?, ?, ?, ?, ?)`, p.Id, p.Author, p.UDesc, p.Title, p.Content, p.Summary, p.Date, p.Updated, p.Category)
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
	u := new(models.User)
	
	var isAdmin int
	
	intPid, err := strconv.Atoi(pid)
	if err != nil {
		return c.String(http.StatusInternalServerError, "게시글이 존재하지 않습니다.")	
	}
	
	// Database에서 게시글 쿼리
	err = db.QueryRow("SELECT * FROM posts WHERE id = ?", intPid).Scan(&p.Id, &p.Author, &p.UDesc, &p.Title, &p.Content, &p.Summary, &p.Date, &p.Updated, &p.Category)

	// 세션에서 현재 유저 정보 가져오기
	sess, err := session.Get("session", c)
	if err != nil {
		log.Println(err)
	}
	
	log.Println(len(sess.Values))
	
	// 로그인정보가 존재하는 경우
	if (len(sess.Values) > 1) {
		_, err = u.GetUser(sess)
		if err != nil {
			log.Println(err)
		}
		
		// Database에 유저 세션정보가 있는지 확인
		if (u.Check(db, "ID")) {			
			isAdmin = u.Admin
		} else {
			isAdmin = 0
		}
	} else {
		isAdmin = 0
	}
	
	log.Println(u.Admin)
	log.Println(u.Name)
	
	log.Println(isAdmin)
	
	
	
	return c.Render(http.StatusOK, "blog/post.html", map[string]interface{}{
		"PageTitle": "DevLee",
		"Post": p,
		"Admin": isAdmin,
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
