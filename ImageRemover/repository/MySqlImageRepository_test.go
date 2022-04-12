package repository

import (
	"ImageRemover/model"
	"database/sql"
	"github.com/DATA-DOG/go-sqlmock"
	"github.com/jmoiron/sqlx"
	"github.com/stretchr/testify/assert"
	"sync"
	"testing"
	"time"
)

type mockDB struct {
	db   *sql.DB
	mock sqlmock.Sqlmock
}

var once sync.Once
var instance *mockDB

func mockDb() *mockDB {
	once.Do(func() {
		db, mock, err := sqlmock.New(sqlmock.QueryMatcherOption(sqlmock.QueryMatcherEqual))
		if err != nil {
			panic(err)
		}
		instance = &mockDB{db: db, mock: mock}
	})

	return instance
}

func getRepo() *MySqlImageRepository {
	mockdb := mockDb()

	return &MySqlImageRepository{
		sqlx.NewDb(mockdb.db, "sqlmock"),
	}
}

func TestMySqlImageRepository_FindStatusPending(t *testing.T) {
	repo := getRepo()
	mock := mockDb().mock
	img := model.Image{
		22,
		"test2.jpg",
		"imageURL2",
		time.Date(2020, time.January, 1, 0, 0, 0, 0, time.UTC),
		//time.Now(),
		recordStatusPending,
	}

	rows := sqlmock.NewRows([]string{"id", "origin_name", "url", "create_at", "status"}).
		AddRow(img.Id, img.OriginName, img.Url, img.CreateAt, img.Status)

	mock.ExpectQuery("SELECT * FROM images WHERE TIMESTAMPDIFF(HOUR, create_at, now()) >= ? AND status = ?").
		WithArgs(2 * 24, recordStatusPending).
		WillReturnRows(rows)

	_, err := repo.FindStatusPending(2 * 24)
	assert.Nil(t, err)

	err = mock.ExpectationsWereMet()
	assert.Nil(t, err)
}