package com.java_api.controller.dto.url;

import org.springframework.data.domain.Slice;

import java.util.List;

public record PageResponseDTO<T>(
        List<T> content,
        int currentPage,
        int pageSize,
        boolean hasNext,
        boolean hasPrevious
) {
    public PageResponseDTO(Slice<T> slice) {
        this (
            slice.getContent(),
            slice.getNumber(),
            slice.getSize(),
            slice.hasNext(),
            slice.hasPrevious()
        );
    }
}
