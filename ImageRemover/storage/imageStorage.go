package storage

import "ImageRemover/model"

type ImageStorage interface {
	Remove(image model.Image) error
}