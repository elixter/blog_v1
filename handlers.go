package main

import (
	"database/sql"
	sync2 "sync"

	// go standard libraries
	"net/http"
	"regexp"
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
	"github.com/grokify/html-strip-tags-go"
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
)

type page struct {
	CurrentPage int
	Length int
}

// db에서 카테고리 가져오는 함수
func getCategories(db *sql.DB) ([]string, error) {
	var categories []string

	// Database에서 카테고리 가져오기
	rows, err := db.Query("select category from categories;")
	if err != nil {
		log.Println(err)
	}

	for rows.Next() {
		var tmp string
		err = rows.Scan(&tmp)
		if err != nil {
			log.Println(err)
		}
		categories = append(categories, tmp)
	}

	return categories, err
}

func convertHashTag(hashTags string) []string {
	// "#해쉬태그 #테스트" 와 같은 문자열을
	// [해쉬태그, 테스트] 로 바꿔주는 함수
	regExp := regexp.MustCompile("#")

	regedHash := regExp.ReplaceAllLiteralString(hashTags, "")		// 정규화된 해쉬태그들

	return strings.Split(regedHash, " ")		// 공백으로 토큰화하여 리턴
}

func createSummary(p models.Post) string {
	// p.Content에서 특정길이 문자열에서 html태그 제거한것
	var sumText string
	if len(p.Content) >= summaryLength {
		if (strings.Contains(p.Content, "&nbsp;")) {
			sumText = strings.Split(p.Content, "&nbsp;")[0]
		} else {
			sumText = p.Content[:summaryLength]
		}
	}  else {
		if (strings.Contains(p.Content, "&nbsp;")) {
			sumText = strings.Split(p.Content, "&nbsp;")[0]
		} else {
			sumText = p.Content
		}
	}

	return sumText
}


func createThumbnail(content string) string {
	var thumbnail string
	contentDoc, err := goquery.NewDocumentFromReader(strings.NewReader(content))
	if err != nil {
		log.Println(err)
	}
	firstImage := contentDoc.Find("img").First()
	log.Println("img태그 가져오기")
	if len(firstImage.Nodes) != 0 {
		thumbnail = firstImage.Nodes[0].Attr[src].Val
	} else {
		thumbnail = ""
	}
	
	log.Println("썸네일 추출 완료")

	return thumbnail 
}

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
	var posts []models.Post
	var isAdmin int

	var err error
	var thumbnail sql.NullString
	var sync sync2.WaitGroup

	// 카테고리
	var categories []string
	go func() {
		categories, _ = getCategories(db)
	}()

	u := new(models.User)

	// 현재 페이지 값
	// Pagination
	currentPage := c.FormValue("currentPage")
	if currentPage == "" {
		// 초기Request땐 1번으로 지정.
		currentPage = "1"
	}

	var totalPost int

	sync.Add(1)
	go func () {
		_ = db.QueryRow("select count(*) as totalposts from posts;").Scan(&totalPost)
		sync.Done()
	}()

	cpInt, _ := strconv.Atoi(currentPage)
	fmt.Println(currentPage)

	sync.Wait()
	Pagination := page{cpInt, totalPost / RowsPerPage + 1}
	if totalPost == 0 {
		Pagination.Length = 0
	}
	// 현재 페이지에 해당하는 게시글만 쿼리
	Rows, err := db.Query("select * from posts where id > ? and id  <= ? order by id desc;", (cpInt - 1) * RowsPerPage, cpInt * RowsPerPage)
	//Rows, err := db.Query("select * from posts order by id desc;")
	if err != nil {
		log.Fatal(err)
	}
	defer Rows.Close()
	
	for Rows.Next() {
		p := models.Post{}
		var hashs sql.NullString		// 하나로 합쳐져있는 해쉬태그 문자열
		err := Rows.Scan(&p.Id, &p.Author, &p.UDesc, &p.Title, &thumbnail, &p.Content, &p.Summary, &p.Date, &p.Updated, &p.Category, &hashs)
		if err != nil {
			log.Println(err)
		}

		p.HashTags = convertHashTag(hashs.String)
		p.Thumbnail = thumbnail.String
		// posts에 새로 받아온 post append
		posts = append(posts, p)
	}
	
	// 세션에서 현재 유저 정보 가져오기
	sess, err := session.Get(UserSession, c)
	if err != nil {
		log.Println(err)
	}
	
	u, err = models.GetUser(sess, CurrentUserKey)
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
		categories, _ := getCategories(db)

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
		log.Println("데이터 바인딩할때 체크")
		// Summary 만들기
		// p.Content에서 특정길이 문자열에서 html태그 제거한것
		sumText := createSummary(*p)
		
		
		t := time.Now() // 현재시간 가져오기
		log.Println("에러 체크 DB에 넣기전")
		// Post객체에 각 데이터 넣기
		p.Author = u.Name
		p.UDesc = u.Desc 
		p.Date = t
		p.Updated = t
		p.Summary = strip.StripTags(sumText)
		p.Category = c.FormValue("category")
		hashTags := c.FormValue("hash")
		p.Thumbnail = createThumbnail(p.Content)
		
		
		// DB insert query
		_, err = db.Exec(`insert into posts values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`, p.Id, p.Author, p.UDesc, p.Title, p.Thumbnail, p.Content, p.Summary, p.Date, p.Updated, p.Category, hashTags)
		if err != nil {
			log.Fatal(err)
		}

		log.Printf("Post \"%s\" is posted on %s\n", p.Title, p.Date.Format("2006-01-02 15:04:05"))
		
		return c.Redirect(http.StatusMovedPermanently, "/blog")
	}
	
	return c.String(http.StatusBadRequest, "bad")
}

// 게시글 view
func ServePost (c echo.Context) error {

	pid := c.QueryParam("id")
	p := new(models.Post)
	
	var isAdmin int
	var hashs sql.NullString		// 하나로 합쳐진 해쉬태그 문자열
	var thumbnail sql.NullString

	// 카테고리 가져오기
	categories, _ := getCategories(db)

	intPid, err := strconv.Atoi(pid)
	if err != nil {
		return c.String(http.StatusInternalServerError, "게시글이 존재하지 않습니다.")	
	}
	
	// Database에서 게시글 쿼리
	err = db.QueryRow("SELECT * FROM posts WHERE id = ?", intPid).Scan(&p.Id, &p.Author, &p.UDesc, &p.Title, &thumbnail, &p.Content, &p.Summary, &p.Date, &p.Updated, &p.Category, &hashs)
	p.HashTags = convertHashTag(hashs.String)
	p.Thumbnail = thumbnail.String
	
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
	
	intPid, err := strconv.Atoi(pid)
	if err != nil {
		return c.String(http.StatusInternalServerError, "id 정수형 변환 실패")	
	}

	if (c.Request().Method == "GET") {	
		// Request method가 GET인 경우
		err = db.QueryRow("SELECT title, content FROM posts WHERE id = ?", intPid).Scan(&p.Title, &p.Content)
		categories, err := getCategories(db)
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
		p.Thumbnail = createThumbnail(p.Content)
		hashTags := c.FormValue("hash")
		
		// DB Update
		_, err := db.Exec("update posts set title = ?,thumbnail = ?, content = ?, category = ?, hashtag = ?, updated = ? where id = ?", p.Title, p.Thumbnail, p.Content, p.Category, hashTags, p.Updated, intPid)
		if err != nil {
			log.Fatal(err)
		}
		redirectUrl := "/blog/post?id=" + pid
		
		log.Printf("Redirect to %s\n", redirectUrl)
		
		return c.Redirect(http.StatusFound, redirectUrl)
	}
	
	return c.String(http.StatusBadRequest, "bad")
}

func ConditianalServePosts (c echo.Context) error {
	params := c.QueryParams()
	var posts []models.Post
	var isAdmin int
	var Rows *sql.Rows
	var err error

	pg := new(page)

	// 카테고리
	categories, _ := getCategories(db)

	if params["hash"] != nil {
		// 쿼리 파라미터가 hash일 경우
		str := params["hash"][0]
		str = "%" + str + "%"

		// Pagenation
		currentPage, _ := strconv.Atoi(c.FormValue("currentPage"))
		if currentPage == 0 {
			// 초기Request땐 1번으로 지정.
			currentPage = 1
		}
		_ = db.QueryRow("select count(*) as totalposts from posts where hashtag like ?", str).Scan(&pg.Length)
		pg.CurrentPage = currentPage
		if pg.Length != 0 {
			pg.Length = pg.Length/RowsPerPage + 1
		}
		Rows, err = db.Query(`select id, author, udesc, title, content, summary, date, updated, category, hashtag 
									from (
										select @num := @num + 1 as num,
											p.id, p.author, p.udesc, p.title, p.content, p.summary, p.date, p.updated, p.category, p.hashtag
											from (select @num:=0) a, posts p where hashtag like ?) post
									where post.num > ? and post.num <= ? order by id desc;`, str, (currentPage - 1) * RowsPerPage, currentPage * RowsPerPage)
		if err != nil {
			log.Println(err)
			return err
		}
		defer Rows.Close()
	} else if params["category"] != nil {
		// 쿼리 파라미터가 category일 경우
		str := params["category"][0]

		currentPage, _ := strconv.Atoi(c.FormValue("currentPage"))
		if currentPage == 0 {
			// 초기Request땐 1번으로 지정.
			currentPage = 1
		}

		_ = db.QueryRow("select count(*) as totalposts from posts where category=?", str).Scan(&pg.Length)
		pg.CurrentPage = currentPage
		if pg.Length != 0 {
			pg.Length = pg.Length/RowsPerPage + 1
		}

		Rows, err = db.Query(`select id, author, udesc, title, content, summary, date, updated, category, hashtag 
									from (
										select @num := @num + 1 as num,
										p.id, p.author, p.udesc, p.title, p.content, p.summary, p.date, p.updated, p.category, p.hashtag
										from (select @num:=0) a, posts p where category = ?) post
									where post.num > ? and post.num <= ? order by id desc;`, str, (currentPage - 1) * RowsPerPage, currentPage * RowsPerPage)
		if err != nil {
			log.Println(err)
			return err
		}
		defer Rows.Close()
	}

	for Rows.Next() {
		p := models.Post{}
		var hashs sql.NullString		// 하나로 합쳐져있는 해쉬태그 문자열
		err := Rows.Scan(&p.Id, &p.Author, &p.UDesc, &p.Title, &p.Content, &p.Summary, &p.Date, &p.Updated, &p.Category, &hashs)
		if err != nil {
			log.Println(err)
		}

		p.HashTags = convertHashTag(hashs.String)

		// posts에 새로 받아온 post append
		posts = append(posts, p)
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
		"WriteUrl": "/blog/write",
		"Categories": categories,
		"Pagination": pg,
	})
}