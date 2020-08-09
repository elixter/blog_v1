package main

import (
	// go standard libraries
	"net/http"
	"strconv"
	"fmt"
	"log"
	"time"
	"strings"
	"os"
	"path"
	//"encoding/json"
	
	// open source libraries
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo-contrib/session"
	"github.com/PuerkitoBio/goquery"
	
	// custom libraries
	"models"
)

const (
	RowsPerPage		=	10			// Posts per Page
	pageTitle		=	"Stone Bridge Dev"
	newPost			=	0
	editPost		=	1
	summaryLength	=	300
	
	src			=		2			// In Ckeditor image source's index is 2 with go query selection
	
	writeURL	=	"/blog/write"
)

// index page
func Index (c echo.Context) error{
	// 세션에서 현재 유저 정보 가져오기
	sess, err := session.Get(UserSession, c)
	if err != nil {
		log.Println(err)
	}
	
	u, err := models.GetUser(sess, CurrentUserKey)
	isAdmin := u.GetAdmin(db, sess, CurrentUserKey)
	
	return c.Render(http.StatusOK, "index.html", map[string]interface{}{
		"Admin": isAdmin,
	})
}

func NotFound (c echo.Context) error {
	return c.Render(http.StatusOK, "notfound.html", nil)
}

// 블로그 첫 화면에 쓰이는 데이터 serving
func ServePosts (c echo.Context) error {
	var isAdmin int
	var err error
	u := new(models.User)

	categories, err := models.GetCategories(db)
	if err != nil {
		log.Println(err)
	}
	
	// Pagination
	// Current page
	currentPage := c.FormValue("currentPage")
	if currentPage == "" {
		// 초기Request땐 1번으로 지정.
		currentPage = "1"
	}

	postCount, err := models.GetPostCount(db)
	if err != nil {
		log.Println(err)
	}
	
	// Convert string type currentPage to integer type
	currentPageI, _ := strconv.Atoi(currentPage)
	Pagination := models.Page{currentPageI, postCount / RowsPerPage + 1}

	// 게시글 가져오기
	posts, err := models.GetCurrentPagePosts(db, currentPageI, RowsPerPage)
	if err != nil {
		log.Println(err)
	}
	
	// 세션에서 현재 유저 정보 가져오기
	sess, err := session.Get(UserSession, c)
	if err != nil {
		log.Println(err)
	}
	
	u, err = models.GetUser(sess, CurrentUserKey)
	if err != nil {
		log.Println(err)
	}
	
	isAdmin = u.GetAdmin(db, sess, CurrentUserKey)
	fmt.Printf("isAdmin : %d\n", isAdmin)

	// Render.
	return c.Render(http.StatusOK, "blog/index.html", map[string]interface{}{
		"PageTitle": pageTitle,
		"Posts": posts,
		"Admin": isAdmin,
		"WriteUrl": "/blog/write",
		"Categories": categories,
		"Pagination": Pagination,
	})
}

// 글쓰기 화면
func NewPost (c echo.Context) error {
	if c.Request().Method == "GET" {
		// Request method가 GET인 경우

		// Database에서 카테고리 가져오기
		categories, _ := models.GetCategories(db)

		return c.Render(http.StatusOK, "blog/write.html", map[string]interface{}{
			"Url": "/blog/write",
			"CancelUrl": "/",
			"Categories": categories,
			"Admin": 0,				// 어차피 로그인되어야 글을 쓸 수 있어서 0 줬음
		})
	} else if c.Request().Method == "POST" {
		// Request method가 POST인 경우
		p := new(models.Post)
		u := new(models.User)

		// 세션에서 현재 유저 정보 가져오기
		sess, err := session.Get(UserSession, c)
		if err != nil {
			log.Println(err)
		}
		
		u, err = models.GetUser(sess, CurrentUserKey)
		if err != nil {
			log.Println(err)
		}
		
		// Form에서 작성한 게시글 데이터 받아오기
		if err := c.Bind(p); err != nil {
			log.Fatal(err)
			return c.String(http.StatusInternalServerError, "error")
		}

		t := time.Now() // 현재시간 가져오기
		// Post객체에 각 데이터 넣기
		p.Author = u.Name
		p.UDesc = u.Desc 
		p.Date = t
		p.Updated = t
		p.CreateSummary(summaryLength)
		p.Category = c.FormValue("category")
		hashTags := c.FormValue("hash")
		_, err = p.CreateThumbnail()
		if err != nil {
			log.Println(err)
		}
		
		err = p.NewPost(db, hashTags)
		if err != nil {
			log.Println(err)
		}
		
		return c.Redirect(http.StatusMovedPermanently, "/blog")
	}
	
	return c.String(http.StatusBadRequest, "bad")
}

// 게시글 view
func ServePost (c echo.Context) error {
	pid := c.QueryParam("id")
	p := new(models.Post)	
	var isAdmin int

	// 카테고리 가져오기
	categories, err := models.GetCategories(db)
	if err != nil {
		log.Println(err)
	}

	pidI, err := strconv.Atoi(pid)
	if err != nil {
		return c.String(http.StatusInternalServerError, "게시글이 존재하지 않습니다.")	
	}
	
	// Database에서 게시글 쿼리
	err = p.GetPostFromDB(db, pidI)
	
	// 세션에서 현재 유저 정보 가져오기
	sess, err := session.Get(UserSession, c)
	if err != nil {
		log.Println(err)
	}

	u, err := models.GetUser(sess, CurrentUserKey)
	if err != nil {
		log.Println(err)
	}
	isAdmin = u.GetAdmin(db, sess, CurrentUserKey)
	
	log.Printf("%s(admin: %d)is requested to post %s\n", u.Id, u.Admin, p.Title)
	
	return c.Render(http.StatusOK, "blog/post.html", map[string]interface{}{
		"PageTitle": pageTitle,
		"Post": p,
		"Admin": isAdmin,
		"Id": p.Id,
		"EditUrl": "/blog/edit",
		"DeleteUrl": "/blog/delete",
		"Modify": newPost,
		"Categories": categories,
	})
}


// 게시글 삭제
func DeletePost (c echo.Context) error {
	id := c.QueryParam("id")
	intId, sconvErr := strconv.Atoi(id)

	if sconvErr != nil {
		log.Fatal(sconvErr)
	}
	var pTitle, pContent string		// 어떤 게시글이 삭제되는지 확인용
	
	err := db.QueryRow("select title, content from posts where id = ?", intId).Scan(&pTitle, &pContent)
	if err != nil {
		log.Println(err)
	}
	
	_, err = db.Exec("delete from posts where id = ?", intId)
	if err != nil {
		log.Println(err)
	}

	// auto_increment initialize and sort.
	_, err = db.Exec("ALTER TABLE posts AUTO_INCREMENT=1;")
	if err != nil {
		log.Println(err)
	}

	_, err = db.Exec("SET @COUNT = 0;")
	if err != nil {
		log.Println(err)
	}

	_, err = db.Exec("UPDATE posts SET id = @COUNT := @COUNT+1;")
	if err != nil {
		log.Println(err)
	}
	
	// 이미지파일 제거
	contentDoc, err := goquery.NewDocumentFromReader(strings.NewReader(pContent))
	if err != nil {
		log.Println(err)
	}
	images := contentDoc.Find("img")
	log.Println("img태그 가져오기")
	
	var tmp string
	for i := 0; i < len(images.Nodes); i++ {
		tmp = images.Nodes[i].Attr[src].Val
		log.Println(tmp)
		fErr := os.Remove(path.Base("") + tmp)
		if fErr != nil { 
			log.Println(fErr) 
		}
	}
	
	
	log.Println("썸네일 추출 완료")

	currentTime := time.Now().Format("2006-01-02 15:04:05")
	log.Printf("Post Id %d, Title %s is delete on %s\n", intId, pTitle, currentTime)
	
	return c.Redirect(http.StatusFound, "/")
}

// 게시글 수정
func EditPost (c echo.Context) error {
	p := new(models.Post)		// 수정할 게시글 object
	pid := c.QueryParam("id")
	
	pidI, err := strconv.Atoi(pid)
	if err != nil {
		return c.String(http.StatusInternalServerError, "id 정수형 변환 실패")	
	}

	if (c.Request().Method == "GET") {	
		// Request method가 GET인 경우
		p.GetPostFromDB(db, pidI)
		categories, err := models.GetCategories(db)
		if err != nil {
			log.Println(err)	
		}
		
		return c.Render(http.StatusOK, "blog/write.html", map[string]interface{}{
			"Url": "/blog/edit",
			"Id": pid,
			"Modify": editPost,
			"Categories": categories,
			"EditPost": p,
			"CancelUrl:": "/",
			"Admin": 0,			// 로그인되어야만 수정할 수 있어서 0줌
		})
	} else if (c.Request().Method == "POST") {
		// Request method가 POST인 경우

		if err := c.Bind(p); err != nil {
			log.Fatal(err)
			return c.String(http.StatusInternalServerError, "error")
		}
		
		t := time.Now()
		p.Updated = t
		p.CreateThumbnail()
		hashTags := c.FormValue("hash")
		p.UpdatePost(db, hashTags, pidI)
		redirectUrl := "/blog/post?id=" + pid
		log.Printf("Redirect to %s\n", redirectUrl)
		return c.Redirect(http.StatusFound, redirectUrl)
	}
	
	return c.String(http.StatusBadRequest, "bad")
}

func ConditianalServePosts (c echo.Context) error {
	params := c.QueryParams()
	var isAdmin int
	var posts []models.Post
	var pagination models.Page

	// 카테고리
	categories, _ := models.GetCategories(db)
	// cuurent page
	currentPage := c.FormValue("currentPage")
	if currentPage == "" {
		// 초기Request땐 1번으로 지정.
		currentPage = "1"
	}
	// Convert string type currentPage to integer type
	currentPageI, _ := strconv.Atoi(currentPage)

	if params["hash"] != nil {
		// 쿼리 파라미터가 hash일 경우
		hashTag := params["hash"][0]
		hashTag = "%" + hashTag + "%"

		postCount, err := models.GetPostCountByHashTag(db, hashTag)
		if err != nil {
			log.Println(err)
		}
		pagination = models.Page{currentPageI, postCount / RowsPerPage + 1}
		
		posts, err = models.GetCurrentPagePostsByHashTag(db, currentPageI, RowsPerPage, hashTag)
		if err != nil {
			log.Println(err)
		}
	} else if params["category"] != nil {
		// 쿼리 파라미터가 category일 경우
		category := params["category"][0]

		postCount, err := models.GetPostCountByCategory(db, category)
		if err != nil {
			log.Println(err)
		}
		pagination = models.Page{currentPageI, postCount / RowsPerPage + 1}

		posts, err = models.GetCurrentPagePostsByCategory(db, currentPageI, RowsPerPage, category)
		if err != nil {
			log.Println(err)
		}
	}

	// 세션에서 현재 유저 정보 가져오기
	sess, err := session.Get(UserSession, c)
	if err != nil {
		log.Println(err)
	}
	
	u, err := models.GetUser(sess, CurrentUserKey)
	if err != nil {
		log.Println(err)
	}
	isAdmin = u.GetAdmin(db, sess, CurrentUserKey)

	return c.Render(http.StatusOK, "blog/index.html", map[string]interface{}{
		"PageTitle": pageTitle,
		"Posts": posts,
		"Admin": isAdmin,
		"WriteUrl": writeURL,
		"Categories": categories,
		"Pagination": pagination,
	})
}