package com.java_api.controller.dto.click;

import java.time.LocalDate;

public record DailyClicksDTO(
    LocalDate date,
    Long count
) {}
