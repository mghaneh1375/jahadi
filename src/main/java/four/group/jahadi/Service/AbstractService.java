package four.group.jahadi.Service;

import four.group.jahadi.Exception.InvalidFieldsException;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class AbstractService <T, D> {

    @Autowired
    ModelMapper modelMapper;

    public abstract ResponseEntity<List<T>> list(Object ... filters);

    public abstract void update(ObjectId id, D dto, Object ... params);

    public abstract ResponseEntity<T> store(D dto, Object ... params);

    public abstract ResponseEntity<T> findById(ObjectId id, Object ...params);

    T populateEntity(T t, D d) {
        return (T) modelMapper.map(d, t.getClass());
    }

    void validateString(String val, String key, int min, int max) {
        if (val == null || val.length() < min || val.length() > max)
            throw new InvalidFieldsException(key + " باید حداقل 2 کاراکتر و حداکثر 100 کاراکتر باشد");
    }
}
