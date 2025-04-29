package four.group.jahadi.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FilterableRepositoryImpl<T> implements FilterableRepository<T> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<T> findAllWithFilter(Class<T> typeParameterClass, Filtering filtering) {
        Query query = constructQueryFromFiltering(filtering);
        return mongoTemplate.find(query, typeParameterClass);
    }
}

