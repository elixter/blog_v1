package repository

import (
	"ImageRemover/config"
	"ImageRemover/model"
	"fmt"
	"github.com/jmoiron/sqlx"
	"log"
)

const (
	recordStatusDeleted = "DELETED"
	recordStatusPending = "PENDING"
)

var conf = config.GetConfig()

type MySqlImageRepository struct {
	db *sqlx.DB
}

func New() MySqlImageRepository {
	dbConfig := conf.GetStringMapString("db")
	dataSourceName := fmt.Sprintf(
		"%s:%s@(%s:%s)/%s?parseTime=true",
		dbConfig["account"],
		dbConfig["password"],
		dbConfig["host"],
		dbConfig["port"],
		dbConfig["scheme"],
	)
	dataSource, err := sqlx.Connect(dbConfig["driver"], dataSourceName)
	if err != nil {
		log.Fatal(err)
	}

	return MySqlImageRepository{
		db: dataSource,
	}
}

func (m MySqlImageRepository) DeleteById(id int64) error {
	_, err := m.db.Exec(
		"UPDATE images SET status = ? WHERE id = ?",
		recordStatusDeleted,
		id,
	)

	return err
}

func (m MySqlImageRepository) DeleteByIdBatch(idList []int64) (int64, error) {
	qry, args, err := sqlx.In("UPDATE images SET status = ? WHERE id IN (?)", recordStatusPending, idList)
	if err != nil {
		return 0, err
	}

	result, err := m.db.Exec(
		qry,
		args,
	)
	if err != nil {
		return 0, err
	}

	return result.RowsAffected()
}

func (m MySqlImageRepository) FindStatusPending(expire int) ([]model.Image, error) {
	var result []model.Image
	err := m.db.Select(
		&result,
		"SELECT * FROM images WHERE DATEDIFF(now(), create_at) > ? AND status = ?",
		expire,
		recordStatusPending,
	)
	if err != nil {
		return nil, err
	}

	return result, nil
}

