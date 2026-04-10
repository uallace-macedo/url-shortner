package config

import (
	"io"
	"log"
	"os"
)

type Logger struct {
	debug   *log.Logger
	info    *log.Logger
	warning *log.Logger
	err     *log.Logger
	writer  io.Writer
}

const (
	reset  = "\033[0m"
	red    = "\033[31m"
	green  = "\033[32m"
	yellow = "\033[33m"
	blue   = "\033[34m"
)

func NewLogger(p string) *Logger {
	writer := io.Writer(os.Stdout)
	logger := log.New(writer, p, log.Ldate|log.Ltime)

	return &Logger{
		debug:   log.New(logger.Writer(), blue+"[DEBUG] "+reset, logger.Flags()),
		info:    log.New(logger.Writer(), green+"[INFO] "+reset, logger.Flags()),
		warning: log.New(logger.Writer(), yellow+"[WARNING] "+reset, logger.Flags()),
		err:     log.New(logger.Writer(), red+"[ERROR] "+reset, logger.Flags()),
		writer:  logger.Writer(),
	}
}

func (l *Logger) Debug(v ...any) {
	l.debug.Println(v...)
}

func (l *Logger) Info(v ...any) {
	l.info.Println(v...)
}

func (l *Logger) Warning(v ...any) {
	l.warning.Println(v...)
}

func (l *Logger) Error(v ...any) {
	l.err.Println(v...)
	os.Exit(1)
}

func (l *Logger) Debugf(fmts string, v ...any) {
	l.debug.Printf(fmts, v...)
}

func (l *Logger) Infof(fmts string, v ...any) {
	l.info.Printf(fmts, v...)
}

func (l *Logger) Warningf(fmts string, v ...any) {
	l.warning.Printf(fmts, v...)
}

func (l *Logger) Errorf(fmts string, v ...any) {
	l.err.Printf(fmts, v...)
	os.Exit(1)
}
