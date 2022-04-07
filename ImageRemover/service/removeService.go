package service

type RemoveService interface {
	Remove() (int64, error)
}
