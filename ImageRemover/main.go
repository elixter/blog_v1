package main

import "time"

func main() {
	duration, _ := time.ParseDuration("1m")
	ticker := time.NewTicker(duration)
	defer ticker.Stop()

	Init()
}
