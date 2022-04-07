package main

import (
	"ImageRemover/imageRemove"
	_ "github.com/go-sql-driver/mysql"
)

func main() {
	remover := imageRemove.New()
	remover.Run()
}
