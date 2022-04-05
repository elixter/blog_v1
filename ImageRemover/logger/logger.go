package logger

import (
	"go.uber.org/zap"
	"sync"
)

var instance *zap.SugaredLogger
var once sync.Once

func newLogger() *zap.SugaredLogger {
	logger, _ := zap.NewProduction()
	defer logger.Sync()
	sugar := logger.Sugar()

	return sugar
}

func GetLogger() *zap.SugaredLogger {
	once.Do(func() {
		instance = newLogger()
	})

	return instance
}
