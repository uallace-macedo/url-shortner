package config

import (
	"os"

	"github.com/joho/godotenv"
)

type Config struct {
	APIPort string

	PSQLHost     string
	PSQLUser     string
	PSQLPassword string
	PSQLDatabase string
	PSQLPort     string
}

func Load() *Config {
	_ = godotenv.Load("../.env")
	return &Config{
		APIPort:      getEnv("GO_REDIRECTOR_PORT", ":4001"),
		PSQLHost:     getEnv("PSQL_HOST", ""),
		PSQLUser:     getEnv("PSQL_USER", ""),
		PSQLPassword: getEnv("PSQL_PASSWORD", ""),
		PSQLDatabase: getEnv("PSQL_DATABASE", ""),
		PSQLPort:     getEnv("PSQL_EXTERNAL_PORT", ""),
	}
}

func getEnv(k, f string) string {
	if v, ok := os.LookupEnv(k); ok {
		return v
	}

	return f
}
