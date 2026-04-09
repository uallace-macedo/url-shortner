package com.java_api.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponse(LocalDateTime timestamp, List<String> errors, String details) {}
