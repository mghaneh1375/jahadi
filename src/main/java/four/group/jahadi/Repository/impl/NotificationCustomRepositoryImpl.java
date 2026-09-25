package four.group.jahadi.Repository.impl;

import four.group.jahadi.DTO.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.FacetOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl {

    private final MongoTemplate mongoTemplate;

    public Page<NotificationDto> findUserNotifications(
            ObjectId userId,
            Boolean seen,
            Pageable pageable
    ) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(
                Aggregation.match(
                        Criteria.where("type").is("SITE")
                )
        );

        operations.add(
                Aggregation.lookup(
                        "trip",
                        "area_id",
                        "areas._id",
                        "trip"
                )
        );

        operations.add(
                Aggregation.unwind("trip")
        );
        operations.add(
                Aggregation.unwind("trip.areas")
        );

        operations.add(
                context -> new Document(
                        "$match",
                        new Document(
                                "$expr",
                                new Document(
                                        "$eq",
                                        Arrays.asList(
                                                "$trip.areas._id",
                                                "$area_id"
                                        )
                                )
                        )
                )
        );

        operations.add(
                Aggregation.match(
                        Criteria.where("trip.areas.members")
                                .in(userId)
                )
        );

        operations.add(
                context -> new Document(
                        "$addFields",
                        new Document("seenStatus",
                                new Document(
                                        "$in",
                                        Arrays.asList(
                                                userId,
                                                "$seen_by"
                                        )
                                )
                        )
                                .append(
                                        "areaName",
                                        "$trip.areas.name"
                                )
                )
        );

        if(seen != null){
            if(seen){
                operations.add(
                        Aggregation.match(
                                Criteria.where("seen_by")
                                        .in(userId)
                        )
                );
            } else{
                operations.add(
                        Aggregation.match(
                                Criteria.where("seen_by")
                                        .not()
                                        .in(userId)
                        )
                );

            }
        }

        operations.add(
                Aggregation.sort(
                        Sort.Direction.DESC,
                        "created_at"
                )
        );

        FacetOperation facet =
                Aggregation.facet()
                        .and(
                                Aggregation.skip(pageable.getOffset()),
                                Aggregation.limit(pageable.getPageSize())
                        )
                        .as("data")
                        .and(
                                Aggregation.count().as("count")
                        )
                        .as("count");


        operations.add(facet);

        Aggregation aggregation =
                Aggregation.newAggregation(operations);

        Document result =
                mongoTemplate.aggregate(
                                aggregation,
                                "notification",
                                Document.class
                        )
                        .getUniqueMappedResult();

        List<Document> docs =
                result.getList("data", Document.class);

        List<NotificationDto> content =
                docs.stream()
                        .map(doc -> mongoTemplate
                                .getConverter()
                                .read(NotificationDto.class, doc))
                        .collect(Collectors.toList());

        long total = 0;
        List<Document> count =
                result.getList("count", Document.class);

        if(count != null && !count.isEmpty()) {
            total =
                    count.get(0).getInteger("count");
        }

        return new PageImpl<>(
                content,
                pageable,
                total
        );
    }

    public long countUnreadNotifications(ObjectId userId) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(
                Aggregation.match(
                        Criteria.where("type").is("SITE")
                )
        );

        operations.add(
                Aggregation.lookup(
                        "trip",
                        "area_id",
                        "areas._id",
                        "trip"
                )
        );

        operations.add(
                Aggregation.unwind("trip")
        );

        operations.add(
                Aggregation.unwind("trip.areas")
        );

        operations.add(
                context -> new Document(
                        "$match",
                        new Document(
                                "$expr",
                                new Document(
                                        "$eq",
                                        Arrays.asList(
                                                "$trip.areas._id",
                                                "$area_id"
                                        )
                                )
                        )
                )
        );

        operations.add(
                Aggregation.match(
                        Criteria.where("trip.areas.members")
                                .in(userId)
                )
        );

        operations.add(
                Aggregation.match(
                        Criteria.where("seen_by")
                                .not()
                                .in(userId)
                )
        );

        operations.add(
                Aggregation.count()
                        .as("count")
        );

        Aggregation aggregation =
                Aggregation.newAggregation(operations);

        Document result =
                mongoTemplate.aggregate(
                                aggregation,
                                "notification",
                                Document.class
                        )
                        .getUniqueMappedResult();

        if (result == null) {
            return 0;
        }

        return result.getInteger("count", 0);
    }

}
