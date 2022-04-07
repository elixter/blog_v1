package repository

import "ImageRemover/model"

type ImageRepository interface {
	DeleteById(id int64) error
	DeleteByIdBatch(idList []int64) (int64, error)

	FindStatusPending(expire int) ([]model.Image, error)
}
