package repository

import (
	"ImageRemover/model"
	"fmt"
	"github.com/jmoiron/sqlx"
)

const (
	recordStatusDeleted = "DELETED"
	recordStatusPending = "PENDING"
)

type MySqlImageRepository struct {
	db *sqlx.DB
}

func New(db *sqlx.DB) MySqlImageRepository {
	return MySqlImageRepository{
		db: db,
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
	idQueryString := fmt.Sprintf("%#v", idList)
	tp := fmt.Sprintf("%T", idList)
	idQueryString = fmt.Sprintf("(%s)", idQueryString[len(tp) + 1:len(idQueryString) - 1])

	_, err := m.db.Query(
		"UPDATE images SET status = ? WHERE id IN " + idQueryString,
		recordStatusDeleted,
	)
	if err != nil {
		return 0, err
	}

	return 0, nil
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

