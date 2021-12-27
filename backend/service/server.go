package service

import (
	"database/sql"
	"github.com/gorilla/sessions"
	"github.com/labstack/echo/v4"
)

type Service struct {
	EchoRouter   *echo.Echo
	DB           *sql.DB
	SessionStore *sessions.CookieStore
}

func (s *Service) Close() {
	s.DB.Close()
}
