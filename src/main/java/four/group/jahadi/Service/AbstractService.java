package four.group.jahadi.Service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class AbstractService <T> {

    public abstract ResponseEntity<List<T>> list(Object ... filters);

    public abstract ResponseEntity<T> findById(ObjectId id, Object ...params);

}
