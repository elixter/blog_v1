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

func TestMySqlImageRepository_DeleteById(t *testing.T) {

}

func TestMySqlImageRepository_DeleteByIdBatch(t *testing.T) {
	type fields struct {
		db *sqlx.DB
	}
	type args struct {
		idList []int64
	}
	tests := []struct {
		name    string
		fields  fields
		args    args
		want    int64
		wantErr bool
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			m := MySqlImageRepository{
				db: tt.fields.db,
			}
			got, err := m.DeleteByIdBatch(tt.args.idList)
			if (err != nil) != tt.wantErr {
				t.Errorf("DeleteByIdBatch() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if got != tt.want {
				t.Errorf("DeleteByIdBatch() got = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestMySqlImageRepository_FindStatusPending(t *testing.T) {
	mockdb := mockDb()

	repo := MySqlImageRepository{
		sqlx.NewDb(mockdb.db, "sqlmock"),
	}

	img := model.Image{
		21,
		"test.jpg",
		"imageURL",
		time.Now(),
		recordStatusPending,
	}

	rows := sqlmock.NewRows([]string{"id", "origin_name", "url", "create_at", "status"}).
		AddRow(img.Id, img.OriginName, img.Url, img.CreateAt, img.Status)

	mockdb.mock.ExpectQuery("SELECT * FROM images WHERE DATEDIFF(now(), create_at) > ? AND status = ?").
		WithArgs(2, recordStatusPending).
		WillReturnRows(rows)

	images, err := repo.FindStatusPending(2)
	if err != nil {
		t.Fatal(err)
	}
	assert.Nil(t, err)

	err = mockdb.mock.ExpectationsWereMet()
	if err != nil {
		t.Fatal(err)
	}
	assert.Nil(t, err)

	assert.Contains(t, images, img)
}
