package User

type Grade int

const (
	Admin Grade = 1 + iota
	Regular
)

type User struct {
	Id int64 `json:"id"`
	Name string `json:"name"`
	Grade Grade `json:"grade"`
}