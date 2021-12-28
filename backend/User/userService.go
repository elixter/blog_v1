package User

type Service interface {
	Join(user User) error

	FindUser(userId int64) (User, error)
}