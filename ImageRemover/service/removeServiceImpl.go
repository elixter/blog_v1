package service

import (
	"ImageRemover/config"
	"ImageRemover/repository"
	"ImageRemover/storage"
	"sync"
)

var conf = config.GetConfig()

type RemoveServiceImpl struct {
	repository repository.ImageRepository
	storage    storage.ImageStorage
	expire     int
	wg         *sync.WaitGroup
}

func New(repository repository.ImageRepository, storage storage.ImageStorage) *RemoveServiceImpl {
	expire := conf.GetInt("expire")

	instance := &RemoveServiceImpl{
		repository: repository,
		storage:    storage,
		expire:     expire,
		wg: &sync.WaitGroup{},
	}

	return instance
}

func (rs *RemoveServiceImpl) Remove() (int64, error) {
	images, err := rs.repository.FindStatusPending(rs.expire)
	if err != nil {
		return 0, err
	}

	for _, img := range images {
		rs.wg.Add(1)
		go func(url string) {
			defer rs.wg.Done()
			rs.storage.Remove(url)
		}(img.Url)
	}
	rs.wg.Wait()

	nDeleted, err := rs.repository.Delete(rs.expire)
	if err != nil {
		return 0, err
	}

	return nDeleted, nil
}
