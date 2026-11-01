package com.ecommerce_event_management.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic pagination response wrapper.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;
    private boolean isFirst;

    public static <T> PageResponse<T> from(Page<T> page) {
        return from(page, false);
    }

    public static <T> PageResponse<T> from(Page<T> page, boolean isFetchAll) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(isFetchAll ? -1 : page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(isFetchAll ? 1 : page.getTotalPages())
                .isLast(isFetchAll || page.isLast())
                .isFirst(true)
                .build();
    }
}
