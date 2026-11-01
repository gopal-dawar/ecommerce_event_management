package com.ecommerce_event_management.common.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * CommonFilterDTO: Standard reusable query parameters for all GET / Fetch APIs.
 *
 * Supported Parameters:
 * - Id: Optional filter by primary key
 * - SearchTerm: Multi-column wildcard search (e.g. code, name, description)
 * - PageSize: Number of records per page. If PageSize = -1 (or <= 0), ALL records are returned.
 * - PageNumber: Page index (0-based)
 * - SortBy: Entity property / column name to sort by (e.g. 'id', 'roleName', 'createdDate')
 * - SortOrder: Sort direction ('asc', 'desc', 'ascending', 'descending')
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonFilterDTO {

    @JsonAlias({"Id", "id"})
    private Long id;

    @JsonAlias({"SearchTerm", "searchTerm", "search", "query"})
    private String searchTerm;

    @Builder.Default
    @JsonAlias({"PageNumber", "pageNumber", "page"})
    private Integer pageNumber = 0;

    @Builder.Default
    @JsonAlias({"PageSize", "pageSize", "size"})
    private Integer pageSize = 10;

    @Builder.Default
    @JsonAlias({"SortBy", "sortBy", "sort"})
    private String sortBy = "id";

    @Builder.Default
    @JsonAlias({"SortOrder", "sortOrder", "direction", "order"})
    private String sortOrder = "asc";

    /**
     * Returns true if the client requested all records (PageSize = -1 or <= 0).
     */
    public boolean isFetchAll() {
        return pageSize != null && pageSize <= 0;
    }

    /**
     * Resolves sort direction supporting 'asc', 'desc', 'ascending', 'descending' case-insensitively.
     */
    public Sort.Direction getResolvedSortDirection() {
        if (sortOrder != null) {
            String clean = sortOrder.trim().toLowerCase();
            if (clean.startsWith("desc")) {
                return Sort.Direction.DESC;
            }
        }
        return Sort.Direction.ASC;
    }

    /**
     * Constructs a Spring Data Pageable instance.
     * When isFetchAll() is true, it requests PageRequest.of(0, Integer.MAX_VALUE) with sorting.
     */
    public Pageable toPageable() {
        String sortColumn = (sortBy != null && !sortBy.isBlank()) ? sortBy.trim() : "id";
        Sort sort = Sort.by(getResolvedSortDirection(), sortColumn);

        if (isFetchAll()) {
            return PageRequest.of(0, Integer.MAX_VALUE, sort);
        }

        int page = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;

        return PageRequest.of(page, size, sort);
    }

    // Additional setters for Spring MVC query parameter binding (e.g. ?SearchTerm=... &PageSize=-1)
    public void setId(Long id) { this.id = id; }
    public void setSearchTerm(String searchTerm) { this.searchTerm = searchTerm; }
    public void setPageNumber(Integer pageNumber) { this.pageNumber = pageNumber; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }

    public void setSearch(String search) { this.searchTerm = search; }
    public void setPage(Integer page) { this.pageNumber = page; }
    public void setSize(Integer size) { this.pageSize = size; }
    public void setOrder(String order) { this.sortOrder = order; }
}
