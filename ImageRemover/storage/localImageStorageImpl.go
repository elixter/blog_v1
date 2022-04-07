package storage

import (
	"ImageRemover/model"
	"os"
)

type LocalImageStorageImpl struct {
	imageDir string
}

func NewLocalImageStorageImpl(imageDir string) *LocalImageStorageImpl {
	instance := &LocalImageStorageImpl{
		imageDir: imageDir,
	}

	return instance
}

func (l LocalImageStorageImpl) Remove(image model.Image) error {
	return os.Remove(l.imageDir + "/" + image.OriginName)
}
