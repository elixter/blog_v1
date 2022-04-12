package service

import (
	"ImageRemover/config"
	"ImageRemover/logging"
	"ImageRemover/model"
	"ImageRemover/repository"
	"ImageRemover/storage"
	"sync"
)

var conf = config.GetConfig()
var log = logging.GetLogger()

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
		wg:         &sync.WaitGroup{},
	}

	return instance
}

// TODO: 테스트 코드 작성하면서 go 루틴 유무 벤치마크 측정
func (rs *RemoveServiceImpl) Remove() (int64, error) {
	images, err := rs.repository.FindStatusPending(rs.expire)
	if err != nil {
		return 0, err
	}

	log.Infof("%d peding images detected", len(images))

	var idSlice []int64
	var mutex sync.Mutex
	for _, img := range images {
		rs.wg.Add(1)
		go func(image model.Image) {
			defer rs.wg.Done()
			err := rs.storage.Remove(image)
			if err != nil {
				log.Errorw(
					"failed to remove image",
					"image", image,
					"error msg", err,
				)
				return
			}
			mutex.Lock()
			idSlice = append(idSlice, img.Id)
			mutex.Unlock()
		}(img)
	}
	rs.wg.Wait()

	if len(idSlice) == 0 {
		return 0, nil
	}

	nDeleted, err := rs.repository.DeleteByIdBatch(idSlice)
	if err != nil {
		return 0, err
	}

	return nDeleted, nil
}
