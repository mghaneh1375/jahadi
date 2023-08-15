package four.group.jahadi.Service;

import four.group.jahadi.Models.PaginatedResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.util.List;

public abstract class AbstractService <T, D> {

    PaginatedResponse<T> returnPaginateResponse(Page<T> all) {
        return PaginatedResponse.<T>builder()
                .currentPage(all.getNumber())
                .totalItems(all.getTotalElements())
                .totalPages(all.getTotalPages())
                .items(all.getContent())
                .hasNext(all.hasNext())
                .build();
    }

    abstract PaginatedResponse<T> list(List<String> filters);

    abstract String update(ObjectId id, D dto);

    abstract String store(D dto);

    abstract T populateEntity(T t, D d);

    abstract T findById(ObjectId id);

}
