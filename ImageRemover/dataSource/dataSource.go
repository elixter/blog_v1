package dataSource

import (
	"ImageRemover/config"
	"ImageRemover/logging"
	"database/sql"
	"sync"
)

var once sync.Once
var log = logging.GetLogger()
var conf = config.GetConfig()

type DataSource interface {
	Get() *sql.DB
	Close() error
	GetDriverName() string
}
