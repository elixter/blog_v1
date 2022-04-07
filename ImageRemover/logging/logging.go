package logging

import (
	"go.uber.org/zap"
	"sync"
)

var instance *zap.SugaredLogger
var once sync.Once

func newLogging() *zap.SugaredLogger {
	logger, _ := zap.NewDevelopment()
	defer logger.Sync()
	sugar := logger.Sugar()

	return sugar
}

func GetLogger() *zap.SugaredLogger {
	once.Do(func() {
		instance = newLogging()
	})

	return instance
}
