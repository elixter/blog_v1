package imageRemove

import (
	"ImageRemover/config"
	"ImageRemover/logging"
	"ImageRemover/service"
	"time"
)

var log = logging.GetLogger()
var conf = config.GetConfig()

type ImageRemove struct {
	removeService service.RemoveService
}

func New(removeService service.RemoveService) ImageRemove {
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
