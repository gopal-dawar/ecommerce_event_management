package com.ecommerce_event_management.common.specification;

import com.ecommerce_event_management.common.dto.CommonFilterDTO;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchSpecification: Reusable JPA Specification builder for common fetch / search APIs.
 */
public final class SearchSpecification {

    private SearchSpecification() {}

    /**
     * Builds a composite Specification combining:
     * 1. Exact ID match (if filter.getId() is specified)
     * 2. Case-insensitive LIKE %searchTerm% across all specified entity fields (e.g. code, name, description)
     */
    public static <T> Specification<T> byFilter(CommonFilterDTO filter, String... searchFields) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filter by ID if present
            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            // 2. SearchTerm across multiple columns (OR condition)
            if (filter.getSearchTerm() != null && !filter.getSearchTerm().trim().isEmpty()) {
                String pattern = "%" + filter.getSearchTerm().trim().toLowerCase() + "%";
                List<Predicate> orPredicates = new ArrayList<>();

                for (String fieldName : searchFields) {
                    try {
                        Path<?> path = resolvePath(root, fieldName);
                        orPredicates.add(cb.like(cb.lower(path.as(String.class)), pattern));
                    } catch (Exception ignored) {
                        // Skip if property does not exist on entity
                    }
                }

                if (!orPredicates.isEmpty()) {
                    predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
                }
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Path<?> resolvePath(Path<?> root, String propertyPath) {
        if (!propertyPath.contains(".")) {
            return root.get(propertyPath);
        }
        Path<?> current = root;
        for (String part : propertyPath.split("\\.")) {
            current = current.get(part);
        }
        return current;
    }
}
