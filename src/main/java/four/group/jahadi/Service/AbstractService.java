package four.group.jahadi.Service;

import four.group.jahadi.Exception.InvalidFieldsException;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractService <T, D> {

    @Autowired
    ModelMapper modelMapper;

    public ResponseEntity<List<T>> list(Object ... filters) {
        return ResponseEntity.ok(
                Collections.emptyList()
        );
    }
    public ResponseEntity<Page<T>> paginateList(int pageIndex, int pageSize, Object ... filters) {
        return ResponseEntity.ok(
                new PageImpl<>(new ArrayList<>(), Pageable.ofSize(pageSize).withPage(pageIndex), 0)
        );
    }

    public abstract void update(ObjectId id, D dto, Object ... params);

    public abstract ResponseEntity<T> store(D dto, Object ... params);

    public abstract ResponseEntity<T> findById(ObjectId id, Object ...params);

    T populateEntity(T t, D d) {
        return (T) modelMapper.map(d, t.getClass());
    }

    void validateString(String val, String key, int min, int max) {
        if (val == null || val.length() < min || val.length() > max)
            throw new InvalidFieldsException(String.format(
                    "%s باید حداقل %d کاراکتر و حداکثر %d کاراکتر باشد",
                    key, min, max
            ));
    }
}
