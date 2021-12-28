package User

type Repository interface {
	FindWithId(userId int64) (User, error)
	InsertUser(user User) error
	DeleteUser(userId int64) error
	UpdateUser(user User) error
}
