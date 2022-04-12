package dataSource

import (
	"database/sql"
	"fmt"
)

var mySqlDataSource *MySqlDataSource

type MySqlDataSource struct {
	driver string
	db     *sql.DB
}

const (
	driverMySql = "mysql"
)

func createMySqlDataSource() (*MySqlDataSource, error) {
	dbConfig := conf.GetStringMapString("db")
	dataSourceName := fmt.Sprintf(
		"%s:%s@(%s:%s)/%s?parseTime=true&timeout=5s",
		dbConfig["account"],
		dbConfig["password"],
		dbConfig["host"],
		dbConfig["port"],
		dbConfig["scheme"],
	)
	dataSource, err := sql.Open(driverMySql, dataSourceName)
	if err != nil {
		return nil, err
	}

	err = dataSource.Ping()
	if err != nil {
		return nil, err
	}

	return &MySqlDataSource{
		driver: driverMySql,
		db: dataSource,
	}, nil
}

func GetMySqlDataSource() (*MySqlDataSource, error) {
	var err error
	err = nil
	once.Do(func() {
		mySqlDataSource, err = createMySqlDataSource()
		if err != nil {
			log.Error(err)
		}
	})

	return mySqlDataSource, err
}

func (m MySqlDataSource) Get() *sql.DB {
	return m.db
}

func (m MySqlDataSource) Close() error {
	return m.db.Close()
}

func (m MySqlDataSource) GetDriverName() string {
	return m.driver
}
