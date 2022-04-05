package service

import "github.com/jmoiron/sqlx"

type RemoveServiceImpl struct {
	db *sqlx.DB
}

func (rs *RemoveServiceImpl) Remove() {

}