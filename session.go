package main

import (
	// go standard libraries
	"log"
	"time"
	"net/http"
	"encoding/json"
	"fmt"
	
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
				log.Println("##세션 생성 완료##")
			} else {
				// Database에 계정의 세션이 있는경우
				// 유효기간 확인해서 만료되었으면 재생성, 유효하면 리턴
				rows, err := db.Query("select * from sessions where uid = ?", u.Id)
				if err != nil {
					log.Println(err)
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
					log.Println("##세션이 유효합니다.##")
					newExp := u.Refresh()
					// Database에서 유효기간 업데이트
					_, err := db.Exec("update sessions set expiresAt = ? where id = ?", newExp, u.Id)
					if err != nil {
						log.Println(err)
						return c.String(http.StatusOK, "데이터베이스 에러")
					}
					
				} else {
					// 세션이 유효하지 않을경우
					log.Println("##세션이 유효하지 않습니다.##")
					UUID := uuid.New()				// 랜덤 uuid로 세션아이디 생성

					// User 객체에 세션아이디, 유효기간 저장
					u.SessionId = UUID.String()
					u.ExpiresAt = t.Add(time.Hour * 2)

					// Database에 세션저장
					_, err = db.Exec("update sessions set id = ?, expiresAt = ? where uid = ?", u.SessionId, u.ExpiresAt, u.Id)
					if err != nil {
						log.Println(err)
						return c.String(http.StatusOK, "데이터베이스 에러")
					}
				}
			}

			// JSON으로 마샬링 후 세션에 탑재
			bind, err := json.Marshal(u)
			if err != nil {
				log.Println(err)
			}

			sess, _ = session.Get("session", c)
			sess.Options = &sessions.Options {
				MaxAge: 3600,		// 1 hour
				HttpOnly: true,
			}
			sess.Values["user"] = bind
			currentPage := fmt.Sprintf("%v", sess.Values["currentPage"])
			// 최근 페이지가 없을경우(최초로그인 등...) index.html로 이동
			if currentPage == "<nil>" {
				currentPage = "/"
			}
			log.Println(currentPage)
			sess.Save(c.Request(), c.Response())
		
			log.Println("##세션저장 완료##")
		
			return c.Redirect(http.StatusMovedPermanently, currentPage)
		default:
		return c.String(http.StatusInternalServerError, "Method is not found")
	}
}


// 인증확인 미들웨어
// 인증이 필요한 화면에 사용해야함.
func AuthHandler (next echo.HandlerFunc) echo.HandlerFunc {
	return func (c echo.Context) error {
		u := new(models.User)
		
		log.Println("인증확인 미들웨어")
		sess, err := session.Get("session", c)
		if err != nil {
			log.Println(err)
			return c.String(http.StatusInternalServerError, "세션을 가져올 수 없습니다.")
		}
		
		// 현재 페이지 저장
		currentURI := c.Request().RequestURI
		if currentURI != "/login" {
			sess.Values["currentPage"] = currentURI
		}

		if (len(sess.Values) <= 1 /* 왜그런지 모르겠지만 기본 길이 1*/) {
			// 쿠키에 세션이 존재하지 않은경우
			log.Println("인증이 안되어있어 로그인페이지로 이동")
			// 현재 페이지를 저장하기위해 세션 Save
			sess.Save(c.Request(), c.Response())
			return c.Redirect(http.StatusMovedPermanently, "/login")
		} else {
			// 쿠키에 세션이 존재하는 경우
			log.Println("세션이 존재하네????")
				
			jUser := sess.Values["user"]
			fmt.Println("받아오기성공")

			err = json.Unmarshal(jUser.([]byte), &u)
			if err != nil {
				log.Println(err)
			}
			
			// 세션 유효기간 확인
			if (u.Valid()) {
				return next(c)
			} else {
				return c.Redirect(http.StatusMovedPermanently, "/login")
			}
		}

		return c.String(http.StatusInternalServerError, "Something is wrong.")
	}
}