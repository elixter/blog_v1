package config

import (
	"ImageRemover/logger"
	"github.com/spf13/viper"
	"go.uber.org/zap"
)

var log *zap.SugaredLogger = logger.GetLogger()

func GetConfig() *viper.Viper {
	vp := viper.New()
	vp.SetConfigFile("config")
	vp.SetConfigType("json")
	vp.AddConfigPath(".")

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

