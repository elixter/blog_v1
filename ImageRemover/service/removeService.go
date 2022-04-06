package service

type RemoveService interface {
	Remove(expire int) (int64, error)
}
