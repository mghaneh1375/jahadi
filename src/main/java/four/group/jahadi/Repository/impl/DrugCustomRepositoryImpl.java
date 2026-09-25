package four.group.jahadi.Repository.impl;

import four.group.jahadi.Enums.Drug.DrugLocation;
import four.group.jahadi.Enums.Drug.DrugType;
import four.group.jahadi.Models.Drug;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugCustomRepositoryImpl {

    private final MongoTemplate mongoTemplate;

    public Page<Drug> findDrugsAdvanced(
            ObjectId groupId,
            String name,
            Integer availableMin,
            Integer availableMax,
            DrugLocation location,
            DrugType drugType,
            LocalDateTime expireFrom,
            LocalDateTime expireTo,
            String boxNo,
            String shelfNo,
            Pageable pageable
    ) {
        List<Criteria> andCriteria = new ArrayList<>();
        andCriteria.add(
                new Criteria().orOperator(
                        Criteria.where("deleted_at").is(null),
                        Criteria.where("deleted_at").exists(false)
                )
        );

        if (groupId != null) {
            andCriteria.add(Criteria.where("group_id").is(groupId));
        }

        if (name != null && !name.isBlank()) {
            andCriteria.add(
                    new Criteria().orOperator(
                            Criteria.where("name").regex(name, "i"),
                            Criteria.where("code").regex(name, "i")
                    )
            );
        }

        if (availableMin != null || availableMax != null) {
            Criteria availableCriteria = Criteria.where("available");

            if (availableMin != null) {
                availableCriteria.gte(availableMin);
            }

            if (availableMax != null) {
                availableCriteria.lte(availableMax);
            }

            andCriteria.add(availableCriteria);
        }

        if (location != null) {
            andCriteria.add(Criteria.where("location").is(location));
        }

        if (drugType != null) {
            andCriteria.add(Criteria.where("drug_type").is(drugType));
        }

        if (expireFrom != null || expireTo != null) {
            Criteria expireCriteria = Criteria.where("expire_at");

            if (expireFrom != null) {
                expireCriteria.gte(expireFrom);
            }

            if (expireTo != null) {
                expireCriteria.lte(expireTo);
            }

            andCriteria.add(expireCriteria);
        }

        if (boxNo != null) {
            andCriteria.add(Criteria.where("box_no").is(boxNo));
        }

        if (shelfNo != null) {
            andCriteria.add(Criteria.where("shelf_no").is(shelfNo));
        }

        Criteria criteria = new Criteria().andOperator(
                andCriteria.toArray(new Criteria[0])
        );

        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, Drug.class);

        query.with(pageable);
        List<Drug> content = mongoTemplate.find(query, Drug.class);

        return new PageImpl<>(content, pageable, total);
    }

    public void archive(ObjectId id, ObjectId groupId) {
        Query query = new Query(Criteria.where("_id").is(id));
        query.addCriteria(Criteria.where("group_id").is(groupId));
        Update update = new Update().set("deleted_at", new Date());

        mongoTemplate.updateFirst(query, update, Drug.class);
    }

    public void archiveAll(List<ObjectId> ids, ObjectId groupId) {
        Query query = new Query(Criteria.where("_id").in(ids));
        query.addCriteria(Criteria.where("group_id").is(groupId));
        Update update = new Update().set("deleted_at", new Date());

        mongoTemplate.updateMulti(query, update, Drug.class);
    }
}