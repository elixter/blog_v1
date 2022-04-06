package repository

import "ImageRemover/model"

type ImageRepository interface {
	Delete(expire int) (int64, error)
	FindStatusPending(expire int) ([]model.Image, error)
}
