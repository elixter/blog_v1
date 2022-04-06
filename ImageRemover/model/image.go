package model

import "time"

type Image struct {
	Id         int64
	OriginName string
	Url        string
	CreateAt   time.Time
	Status     string
}
