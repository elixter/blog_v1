package repository

import (
	"ImageRemover/model"
	"github.com/jmoiron/sqlx"
)

const (
	recordStatusDeleted = "DELETED"
	recordStatusPending = "PENDING"
)

type MySqlImageRepository struct {
	db *sqlx.DB
}

func (m MySqlImageRepository) Delete(expire int) (int64, error) {
	result, err := m.db.Exec(
		"update images set status = ? where DATEDIFF(now(), create_at) > ? and status = ?",
		recordStatusDeleted,
		expire,
		recordStatusPending,
	)
	if err != nil {
		return 0, err
	}
	nDeleted, _ := result.RowsAffected()

	return nDeleted, nil
}

func (m MySqlImageRepository) FindStatusPending() ([]model.Image, error) {
	panic("implement me")
}

