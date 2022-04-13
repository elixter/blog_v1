package main

import (
	"ImageRemover/config"
	"ImageRemover/imageRemove"
	"ImageRemover/logging"
	"ImageRemover/repository"
	"ImageRemover/repository/dataSource"
	"ImageRemover/service"
	"ImageRemover/storage"
	"github.com/pkg/errors"
)

var imageRepository repository.ImageRepository
var imageStorage storage.ImageStorage
var removeService service.RemoveService

var log = logging.GetLogger()


var conf = config.GetConfig()

func main() {
	remover := imageRemove.New(removeService)
	remover.Run()
}

func init() {
	log.Info("Start initialize")

	imageRepository = repository.GetSqlxImageRepository()
	imageStorage = storage.NewLocalImageStorageImpl(conf.GetString("imagePath"))
	removeService = service.New(imageRepository, imageStorage)
}

func initDataSource() (dataSource.DataSource, error) {
	dbConfig := conf.GetStringMapString("db")
	var result dataSource.DataSource
	var err error

	switch dbConfig["driver"] {
	case "mysql":
		result, err = dataSource.GetMySqlDataSource()
		break
	default:
		return nil, errors.Errorf("Not implemented driver data source %s", dbConfig["driver"])
	}

	return result, err
}
