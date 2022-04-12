package repository

import (
	"ImageRemover/dataSource"
	"ImageRemover/model"
	"github.com/jmoiron/sqlx"
)

var sqlxInstance *SqlxImageRepository

type SqlxImageRepository struct {
	db *sqlx.DB
}

func newSqlxImageRepository(dataSource dataSource.DataSource) *SqlxImageRepository {
	db := dataSource.Get()

	return &SqlxImageRepository{
		db: sqlx.NewDb(db, dataSource.GetDriverName()),
	}
}

func GetSqlxImageRepository(dataSource dataSource.DataSource) *SqlxImageRepository {
	once.Do(func() {
		sqlxInstance = newSqlxImageRepository(dataSource)
	})

	return sqlxInstance
}

func (m SqlxImageRepository) DeleteById(id int64) error {
	_, err := m.db.Exec(
		"UPDATE images SET status = ? WHERE id = ?",
		recordStatusDeleted,
		id,
	)

	return err
}

func (m SqlxImageRepository) DeleteByIdBatch(idList []int64) (int64, error) {
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

func (m SqlxImageRepository) FindStatusPending(expire int) ([]model.Image, error) {
	var result []model.Image
	err := m.db.Select(
		&result,
		"SELECT * FROM images WHERE TIMESTAMPDIFF(HOUR, create_at, now()) >= ? AND status = ?",
		expire,
		recordStatusPending,
	)
	if err != nil {
		return nil, err
	}

	return result, nil
}

