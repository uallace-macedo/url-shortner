package config

import (
	"os"

	"github.com/joho/godotenv"
)

type Config struct {
	APIPort string
}

func Load() *Config {
	_ = godotenv.Load("../.env")
	return &Config{
		APIPort: getEnv("GO_REDIRECTOR_PORT", ":4001"),
	}
}

func getEnv(k, f string) string {
	if v, ok := os.LookupEnv(k); ok {
		return v
	}

	return f
}
