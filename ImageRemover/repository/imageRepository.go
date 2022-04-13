package repository

import (
	"ImageRemover/config"
	"ImageRemover/model"
	"sync"
)

var once sync.Once
var conf = config.GetConfig()

const (
	recordStatusDeleted = "DELETED"
	recordStatusPending = "PENDING"
)

type ImageRepository interface {
	DeleteById(id int64) error
	DeleteByIdBatch(idList []int64) (int64, error)

	FindStatusPending(expire int) ([]model.Image, error)
}
