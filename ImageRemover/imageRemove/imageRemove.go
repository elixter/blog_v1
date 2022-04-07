package imageRemove

import (
	"ImageRemover/config"
	"ImageRemover/logging"
	"ImageRemover/repository"
	"ImageRemover/service"
	"ImageRemover/storage"
	"fmt"
	"github.com/jmoiron/sqlx"
	"time"
)

var log = logging.GetLogger()
var conf = config.GetConfig()

type ImageRemove struct {
	removeService service.RemoveService
}

func New() ImageRemove {
	dbConfig := conf.GetStringMapString("db")
	dataSourceName := fmt.Sprintf(
		"%s:%s@(%s:%s)/%s?parseTime=true",
		dbConfig["account"],
		dbConfig["password"],
		dbConfig["host"],
		dbConfig["port"],
		dbConfig["scheme"],
	)

	db, err := sqlx.Connect(dbConfig["driver"], dataSourceName)
	if err != nil {
		log.Fatal(err)
	}

	imageRepository := repository.New(db)
	imageStorage := storage.NewLocalImageStorageImpl(conf.GetString("imagePath"))
	removeService := service.New(imageRepository, imageStorage)

	return ImageRemove{
		removeService: removeService,
	}
}

func (ir *ImageRemove) Run() {
	duration, _ := time.ParseDuration(conf.GetString("duration"))
	ticker := time.NewTicker(duration)
	defer ticker.Stop()

	log.Info("Start image remover...")
	for {
		select {
		case <-ticker.C:
			log.Info("Start removing images...")
			nDeleted, err := ir.removeService.Remove()
			if err != nil {
				log.Error(err)
				continue
			}

			log.Infof("%d image(s) is deleted successfully", nDeleted)
		}
	}

}
