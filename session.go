package main

import (
	// go standard libraries
	"log"
	"time"
	"net/http"
	
	"models"
	
	// open source libraries
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo-contrib/session"
	"github.com/gorilla/sessions"
	
	"github.com/google/uuid"
)

/*

	uuid를 seesionID로 사용
	Database based session management.
	
	Database : MySQL

	AuthHandler : 로그인이 필요한 페이지에서 로그인이 되어있는지 확인하는 미들웨어

*/


func Login (c echo.Context) error {
	if (c.Request().Method == "GET") {
		// http method가 GET인경우
		return c.Render(http.StatusOK, "/login.html", map[string]interface{}{
			"Url": "/login",
		})
		
	} else if (c.Request().Method == "POST") {
		// http method가 POST인 경우
		
		u := new(models.User)

		id := c.FormValue("userId")
		pw := c.FormValue("userPw")

		saltedPw := "tw" + pw + "blog"			// 임시 salting

		err := db.QueryRow("select uname, admin, udesc from users where uid = ? AND upw = SHA2(?, 256)", id, saltedPw).Scan(&u.Name, &u.Admin, &u.Desc)
		if err != nil {
			return c.String(http.StatusOK, "존재하지않은 아이디이거나 비밀번호가 맞지않습니다.")
		}
		u.Id = id

		UUID := uuid.New()

		// session에 sessionID 추가
		log.Println("Let's create session")
		s := new(models.Session)
		currentTime := time.Now()

		s.Id = UUID.String()
		s.Uid = id
		s.CreatedAt = currentTime
		s.ExpiresAt = currentTime.Add(time.Hour * 2)

		sess, _ := session.Get("session", c)
		sess.Options = &sessions.Options {
			Path: "/",
			MaxAge: 60 * 2,
			HttpOnly: true,
		}
		sess.Values["sessId"] = s.Id
		// currentPage := sess.Values["currentPage"].(string)
		sess.Save(c.Request(), c.Response())
		

		// Database에 이미 Session이 있는지 확인
		log.Println("Let's check session already exist.")
		var count int		// session count
		err = db.QueryRow("select count(id) from sessions where uid = ?", s.Uid).Scan(&count)
		if err != nil {
			log.Println(err)
		}
		
		log.Printf("쿼리결과는 %d개 입니다.", &count)

		if (count >= 1) {
			// 이미 세션이 존재하는 경우
			log.Println("Session already exist.")
			_, err := db.Exec("update sessions set id = ?, expiresAt = ? where uid = ?", s.Id, s.ExpiresAt, s.Uid)
			if err != nil {
				log.Println(err)
				
				return c.String(http.StatusOK, "Session update failed.")
			}
			
			// 이전 페이지로 이동
			return c.Redirect(http.StatusMovedPermanently, "/")
		} else {
			// 세션이 존재하지않는 경우
			log.Println("Session is not exised.")
			// Database에 Session저장
			_, dbErr := db.Exec("insert into sessions values(?, ?, ?, ?)", s.Id, s.Uid, s.CreatedAt, s.ExpiresAt)
			if dbErr != nil {
				log.Println(dbErr)

				return c.String(http.StatusOK, "Row insertion is failed.")
			}
			
			// 이전 페이지로 이동
			return c.Redirect(http.StatusMovedPermanently, "/")
		}
		
		
	}
	
	// 에러 고쳐야됨 씨벌
	
	return c.String(http.StatusOK, "good")
}


func AuthHandler (next echo.HandlerFunc) echo.HandlerFunc {
	return func (c echo.Context) error {
		log.Println("Into middleware")
		sess, err := session.Get("session", c)
		
		if (len(sess.Values) == 0) {
			log.Println("세션없어요시발")
			return c.Redirect(http.StatusMovedPermanently, "/login")
		}
		
		s := new(models.Session)
		
		sess.Values["currentPage"] = c.Request().RequestURI
		s.Id = sess.Values["sessId"].(string)				// session ID
		log.Println(s.Id)
		
		log.Println("쿼리시작")
		err = db.QueryRow("select uid, createdAt, expiresAt from sessions where id = ?", s.Id).Scan(&s.Uid, &s.CreatedAt, &s.ExpiresAt)
		if err != nil {
			log.Println("세션이 존재하지않습니다.")
			log.Println(err)
			return c.Redirect(http.StatusMovedPermanently, "/login")
		}
		log.Println("세션이 존재합니다.")
		log.Println(s.Id)
		
		
		if (s.ExpiresAt.Sub(time.Now()) <= 0) {
			// 세션이 만료되었을 경우
			log.Println("세션 만료되었음")
			_, err := db.Exec("delete from sessions where id = ?", s.Id)		// DB에서 세션삭제
			if err != nil {
				log.Fatal(err)
			}
			
			return c.Redirect(http.StatusMovedPermanently, "/login")
		} else {
			// 세션이 유효한 경우 세션 연장 후 다음 핸들러로 이동
			updateExp := time.Now().Add(time.Hour)		// 한시간 연장
			_, err := db.Exec("update sessions set expiresAt = ? where id = ?", updateExp, s.Id)
			if err != nil {
				log.Fatal(err)
			}
			
			next(c)
		}
		
		return c.String(http.StatusOK, "failed")
	}
}