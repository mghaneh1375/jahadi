package four.group.jahadi.Models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PaginatedResponse<T> {
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private List<T> items;
    private boolean hasNext;
}