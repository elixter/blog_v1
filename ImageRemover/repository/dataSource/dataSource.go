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
var datasource DataSource

type DataSource interface {
	Get() *sql.DB
	Close() error
	GetDriverName() string
}

func GetDataSource() DataSource {
	return datasource
}

func init() {
	dbConfig := conf.GetStringMapString("db")
	var err error

	switch dbConfig["driver"] {
	case "mysql":
		datasource, err = GetMySqlDataSource()
		if err != nil {
			panic(err)
		}
		break
	default:
		log.Errorf("Not implemented driver data source %s", dbConfig["driver"])
		panic(err)
	}
}
