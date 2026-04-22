package config

import (
	"fmt"
	"os"

	"github.com/go-redis/redis/v8"
	"github.com/jmoiron/sqlx"
	"github.com/joho/godotenv"
	"github.com/uallace-macedo/url-shortner/go-redirector/internal/database"
)

type Config struct {
	APIPort string

	PSQLHost     string
	PSQLUser     string
	PSQLPassword string
	PSQLDatabase string
	PSQLPort     string

	REDISHost     string
	REDISPassword string
	REDISPort     string
}

func Load() *Config {
	_ = godotenv.Load("../.env")
	return &Config{
		APIPort: getEnv("GO_REDIRECTOR_PORT", ":4001"),

		PSQLHost:     getEnv("PSQL_HOST", ""),
		PSQLUser:     getEnv("PSQL_USER", ""),
		PSQLPassword: getEnv("PSQL_PASSWORD", ""),
		PSQLDatabase: getEnv("PSQL_DATABASE", ""),
		PSQLPort:     getEnv("PSQL_EXTERNAL_PORT", ""),

		REDISHost:     getEnv("REDIS_HOST", ""),
		REDISPassword: getEnv("REDIS_PASSWORD", ""),
		REDISPort:     getEnv("REDIS_EXTERNAL_PORT", "6379"),
	}
}

func (cfg *Config) getPostgresURL() string {
	return fmt.Sprintf(
		"postgres://%s:%s@%s:%s/%s?sslmode=disable",
		cfg.PSQLUser, cfg.PSQLPassword, cfg.PSQLHost, cfg.PSQLPort, cfg.PSQLDatabase,
	)
}

func (cfg *Config) getRedisURL() string {
	return fmt.Sprintf(
		"%s:%s",
		cfg.REDISHost, cfg.REDISPort,
	)
}

func (cfg *Config) GetDatabases(logger Logger) (*sqlx.DB, *redis.Client) {
	pgDb, err := database.ConnectPostgres(cfg.getPostgresURL())
	if err != nil {
		logger.Errorf("could not connect to db; %v", err)
	}

	logger.Info("pg db connected")

	rdDb, err := database.ConnectRedis(cfg.getRedisURL(), cfg.REDISPassword)
	if err != nil {
		logger.Errorf("could not connect to redis; %v", err)
	}

	logger.Info("redis db connected")
	return pgDb, rdDb
}

func getEnv(k, f string) string {
	if v, ok := os.LookupEnv(k); ok {
		return v
	}

	return f
}
