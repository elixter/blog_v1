package model

import "time"

type Image struct {
	Id         int64     `db:"id"`
	OriginName string    `db:"originName"`
	Url        string    `db:"url"`
	CreateAt   time.Time `db:"create_at"`
	Status     string    `db:"status"`
}
