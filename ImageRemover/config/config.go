package config

import (
	"ImageRemover/logger"
	"github.com/spf13/viper"
	"sync"
)

var log = logger.GetLogger()
var instance *viper.Viper
var once sync.Once

func newConfig() *viper.Viper {
	vp := viper.New()
	vp.SetConfigFile("config.json")
	vp.SetConfigType("json")

	err := vp.ReadInConfig()
	if err != nil {
		if _, ok := err.(viper.ConfigFileNotFoundError); ok {
			log.Errorw(
				"Config file not found",
				"fileName", vp.ConfigFileUsed(),
			)
		} else {
			log.Error(err)
		}
		return nil
	}

	return vp
}

func GetConfig() *viper.Viper {
	once.Do(func() {
		instance = newConfig()
	})

	return instance
}
