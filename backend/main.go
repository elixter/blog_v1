package main

import (
	_ "github.com/go-sql-driver/mysql"
	"golang.org/x/net/http2"
	"log"
	"net/http"
	"time"
)

const (
	maxConcurrentStreamSize = 250
	maxReadFrameSize = 1048576
	idleTimeout = 10 * time.Second
)

func main() {
	router := InitService()

	s := &http2.Server {
		MaxConcurrentStreams: maxConcurrentStreamSize,
		MaxReadFrameSize: maxReadFrameSize,
		IdleTimeout: idleTimeout,
	}

	if err := router.EchoRouter.StartH2CServer(":8080", s); err != http.ErrServerClosed {
		log.Fatal(err)
	}
}