package four.group.jahadi.Repository.impl;

import four.group.jahadi.Repository.FilterableRepository;
import four.group.jahadi.Repository.Filtering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public Page<T> findAllWithFilterWithPagination(
            Class<T> typeParameterClass, Filtering filtering,
            Pageable pageable, Sort sort
    ) {
        Query query = constructQueryFromFiltering(filtering);
        long total = mongoTemplate.count(query, typeParameterClass);

        query.with(pageable);
        query.with(sort);
        List<T> content = mongoTemplate.find(query, typeParameterClass);

        return new PageImpl<>(content, pageable, total);
    }
}

