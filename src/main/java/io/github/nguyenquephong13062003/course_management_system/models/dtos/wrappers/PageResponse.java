package io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A generic wrapper for paginated responses.
 *
 * @param <T> The type of items contained in the paginated response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * The list of items in the current page.
     */
    private List<T> items;

    /**
     * The current page number (0-based).
     */
    private Integer page;

    /**
     * The number of items per page.
     */
    private Integer size;

    /**
     * The total number of items across all pages.
     */
    private Long totalItems;

    /**
     * The total number of pages available.
     */
    private Integer totalPages;

    /**
     * Indicates whether the current page is the last page.
     */
    private Boolean isLast;

}
