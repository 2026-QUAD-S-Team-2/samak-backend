package com.oaosis.samak.global.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CursorResponse<T>(
        @Schema(description = "조회 결과 목록")
        List<T> data,
        @Schema(description = "다음 페이지 커서 (null이면 마지막 페이지)", example = "10")
        Long nextCursor,
        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {
    public static <T> CursorResponse<T> of(List<T> items, int size, CursorIdExtractor<T> idExtractor) {
        boolean hasNext = items.size() > size;
        List<T> data = hasNext ? items.subList(0, size) : items;
        Long nextCursor = hasNext ? idExtractor.extract(data.get(data.size() - 1)) : null;
        return new CursorResponse<>(data, nextCursor, hasNext);
    }

    @FunctionalInterface
    public interface CursorIdExtractor<T> {
        Long extract(T item);
    }
}
