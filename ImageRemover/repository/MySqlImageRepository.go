package repository

import (
	"ImageRemover/logger"
	"github.com/jmoiron/sqlx"
)

var log = logger.GetLogger()

type MySqlImageRepository struct {
	db *sqlx.DB
}

func (m MySqlImageRepository) DeleteById(idList []int64) int {
	result, err := m.db.Exec("delete from images where DATEDIFF(now(), ) > 2")
	if err != nil {
		log.Error(err)
	}
}
