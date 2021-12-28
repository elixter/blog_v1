package User

import "sync"

var storage = map[int64]User{}

type MemoryRepository struct {
	mutex sync.Mutex
}

func (m *MemoryRepository) FindWithId(userId int64) (User, error) {
	m.mutex.Lock()
	defer m.mutex.Unlock()

	if _, ok := storage[userId]; !ok {
		err := Error {
			ErrorMsg: UserNotExistErr,
			ErrorType: UserNotExistErrType,
			FuncName: "FindWithId",
		}
		return User{}, err
	}

	return storage[userId], nil
}

func (m *MemoryRepository) InsertUser(user User) error {
	m.mutex.Lock()
	defer m.mutex.Unlock()

	if _, ok := storage[user.Id]; ok {
		err := Error {
			ErrorMsg: UserAlreadyExistErr,
			ErrorType: UserAlreadyExsitsErrType,
			FuncName: "InsertUser",
		}
		return err
	}
	storage[user.Id] = user

	return nil
}

func (m *MemoryRepository) DeleteUser(userId int64) error {
	m.mutex.Lock()
	defer m.mutex.Unlock()

	if _, ok := storage[userId]; !ok {
		err := Error {
			ErrorMsg: UserNotExistErr,
			ErrorType: UserNotExistErrType,
			FuncName: "DeleteUser",
		}
		return err
	}

	delete(storage, userId)

	return nil
}

func (m *MemoryRepository) UpdateUser(user User) error {
	m.mutex.Lock()
	defer m.mutex.Unlock()

	if _, ok := storage[user.Id]; ok {
		err := Error {
			ErrorMsg: UserNotExistErr,
			ErrorType: UserNotExistErrType,
			FuncName: "UpdateUser",
		}
		return err
	}
	storage[user.Id] = user

	return nil
}