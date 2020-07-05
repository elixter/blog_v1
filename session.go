package main

import (
	// go standard libraries
	"log"
	"time"
	"net/http"
	"encoding/json"
	
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
	switch c.Request().Method {
		case "GET":
			// http method가 GET인경우
			return c.Render(http.StatusOK, "/login.html", map[string]interface{}{
				"Url": "/login",
			})
		case "POST":
			// http method가 POST인 경우
		
			u := new(models.User)

			var sess *sessions.Session
			var sessCheck int			// 세션이 있는지 확인 0이면 없음, 있을경우 나머지...

			id := c.FormValue("userId")
			pw := c.FormValue("userPw")

			saltedPw := "tw" + pw + "blog"			// 임시 salting

			err := db.QueryRow("select uid, uname, admin, udesc from users where uid = ? AND upw = SHA2(?, 256)", id, saltedPw).Scan(&u.Id, &u.Name, &u.Admin, &u.Desc)
			if err != nil {
				log.Println(err)
				return c.String(http.StatusOK, "데이터베이스 에러")
			}

			// 현재 계정으로 등록된 세션이 있는지 확인
			err = db.QueryRow("select count(id) from sessions where uid = ?", u.Id).Scan(&sessCheck)

			t := time.Now()

			if (sessCheck == 0) {
				// Database에 계정의 세션이 없는경우
				// 새로운 세션 생성
				log.Println("##데이터베이스에 세션이 존재하지 않습니다.##")			// 프롬프트 로그
				log.Println("##새로운 세션 생성##")

				UUID := uuid.New()				// 랜덤 uuid로 세션아이디 생성

				// User 객체에 세션아이디, 유효기간 저장
				u.SessionId = UUID.String()
				u.ExpiresAt = t.Add(time.Hour * 2)

				// Database에 세션저장
				_, err := db.Exec("insert into sessions values(?, ?, ?)", u.SessionId, u.Id, u.ExpiresAt)
				if err != nil {
					log.Println(err)
				return c.String(http.StatusOK, "데이터베이스 에러")
				}

			} else {
				// Database에 계정의 세션이 있는경우
				// 유효기간 확인해서 만료되었으면 재생성, 유요하면 리턴
				rows, err := db.Query("select * from sessions where uid = ?", u.Id)
				if err != nil {
					log.Println(err)
					return c.String(http.StatusOK, "데이터베이스 에러")
				}
				defer rows.Close()
				
				for rows.Next() {
					err := rows.Scan(&u.SessionId, &u.Id, &u.ExpiresAt)
					if err != nil {
						log.Println(err)
						return c.String(http.StatusOK, "데이터베이스 에러")
					}
				}

				if (u.Valid()) {
					// 세션이 유효할 경우
					newExp := u.Refresh()
					// Database에서 유효기간 업데이트
					_, err := db.Exec("update sessions set expiresAt = ? where id = ?", newExp, u.Id)
					if err != nil {
						log.Println(err)
						return c.String(http.StatusOK, "데이터베이스 에러")
					}

				} else {
					// 세션이 유효하지 않을경우
					UUID := uuid.New()				// 랜덤 uuid로 세션아이디 생성

					// User 객체에 세션아이디, 유효기간 저장
					u.SessionId = UUID.String()
					u.ExpiresAt = t.Add(time.Hour * 2)

					// Database에 세션저장
					_, err = db.Exec("insert into sessions values(?, ?, ?)", u.SessionId, u.Id, u.ExpiresAt)
					if err != nil {
						log.Println(err)
						return c.String(http.StatusOK, "데이터베이스 에러")
					}
				}
			}

			// JSON으로 마샬링 후 세션에 탑재
			bind, _ := json.Marshal(u)

			sess, _ = session.Get("session", c)
			sess.Options = &sessions.Options {
				MaxAge: 3600 * 2,		// 2 hours
				HttpOnly: true,
			}
			sess.Values["user"] = bind
			currentPage := sess.Values["currentPage"].(string)
			sess.Save(c.Request(), c.Response())
		
			return c.Redirect(http.StatusOK, currentPage)
		default:
		return c.String(http.StatusInternalServerError, "Method is not found")
	}
}


func AuthHandler (next echo.HandlerFunc) echo.HandlerFunc {
	return func (c echo.Context) error {
		s := new(models.Session)
		
		log.Println("인증확인 미들웨어")
		sess, err := session.Get("session", c)
		sess.Values["currentPage"] = c.Request().RequestURI 
		
		// 여기부터 코딩해야됨

		
		if (len(sess.Values) == 0) {
			log.Println("인증이 안되어있어 로그인페이지로 이동")
			return c.Redirect(http.StatusMovedPermanently, "/login")
		}

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