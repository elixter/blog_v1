package User

import "fmt"

const (
	UserNotExistErr = "user not exists"
	UserAlreadyExistErr = "user already exists"
)

const (
	UserNotExistErrType int = 1 + iota
	UserAlreadyExsitsErrType
)

type Error struct {
	FuncName  string
	ErrorMsg  string
	ErrorType int
}

func (e Error) Error() string {
	return fmt.Sprintf("error: %s at %s", e.ErrorMsg, e.FuncName)
}
