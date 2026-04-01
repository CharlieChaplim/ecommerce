package com.ecommerce.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public record PageResponseDTO<T>(
    List<T> content,
    long totalElements,
    int totalPages,
    int pageNumber,
    int pageSize,
    boolean isFirst,
    boolean isLast
) {
    public static <T> PageResponseDTO<T> fromPage(Page<T> page) {
        return new PageResponseDTO<>(
            page.getContent(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize(),
            page.isFirst(),
            page.isLast()
        );
    }
}
