package User

type ServiceImpl struct {
	Repository Repository
}

func (s ServiceImpl) Join(user User) error {
	err := s.Repository.InsertUser(user)
	if err != nil {
		return err
	}

	return nil
}

func (s ServiceImpl) FindUser(userId int64) (User, error) {
	user, err := s.Repository.FindWithId(userId)
	if err != nil {
		return User{}, err
	}

	return user, nil
}
