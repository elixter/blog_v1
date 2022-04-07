package main

import (
	"ImageRemover/repository"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
	"log"
)

func main() {
	//duration, _ := time.ParseDuration("1m")
	//ticker := time.NewTicker(duration)
	//defer ticker.Stop()
	//
	//Init()
	db, err := sqlx.Connect("mysql", "root:971216@(localhost:3306)/blog2?parseTime=true")
	if err != nil {
		log.Fatalln(err)
	}
	repo := repository.New(db)

	result, err := repo.FindStatusPending(0)
	if err != nil {
		log.Fatalln("failed to select", err)
	}

	var idSlice []int64
	for _, res := range result {
		idSlice = append(idSlice, res.Id)
	}

	_, err = repo.DeleteByIdBatch(idSlice)
	if err != nil {
		log.Fatalln("failed to delete", err)
	}
}
