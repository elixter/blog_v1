package repository

type ImageRepository interface {
	DeleteById([]int64) int
}
