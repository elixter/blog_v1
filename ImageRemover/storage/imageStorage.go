package storage

type ImageStorage interface {
	Remove(url string) error
}