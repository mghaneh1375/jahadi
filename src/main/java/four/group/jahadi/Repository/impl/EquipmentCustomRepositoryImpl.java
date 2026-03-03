package four.group.jahadi.Repository.impl;

import four.group.jahadi.Enums.EquipmentHealthStatus;
import four.group.jahadi.Enums.EquipmentType;
import four.group.jahadi.Models.Equipment;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentCustomRepositoryImpl {

    private final MongoTemplate mongoTemplate;

    public Page<Equipment> findAdvanced(
            ObjectId groupId, String name,
            Integer minAvailable, Integer maxAvailable,
            EquipmentHealthStatus healthStatus,
            String propertyId, String location,
            EquipmentType equipmentType,
            String rowNo, String shelfNo,
            LocalDateTime fromBuyAt, LocalDateTime toBuyAt,
            LocalDateTime fromGuaranteeAt, LocalDateTime toGuaranteeAt,
            Pageable pageable
    ) {
        Criteria criteria = new Criteria();
        criteria.andOperator(
                new Criteria().orOperator(
                        Criteria.where("deleted_at").is(null),
                        Criteria.where("deleted_at").exists(false)
                )
        );

        if(groupId != null)
            criteria.and("group_id").is(groupId);

        if (name != null && !name.isEmpty()) {
            criteria.and("name").regex(name, "i");
        }

        if (minAvailable != null) {
            criteria.and("available").gte(minAvailable);
        }

        if (maxAvailable != null) {
            criteria.and("available").lte(maxAvailable);
        }

        if (location != null && !location.isEmpty()) {
            criteria.and("location").regex(location, "i");
        }

        if (equipmentType != null) {
            criteria.and("equipment_type").is(equipmentType);
        }

        if (healthStatus != null) {
            criteria.and("health_status").is(healthStatus);
        }

        if (propertyId != null) {
            criteria.and("property_id").is(propertyId);
        }

        if (rowNo != null) {
            criteria.and("row_no").is(rowNo);
        }

        if (shelfNo != null) {
            criteria.and("shelf_no").is(shelfNo);
        }

        if (fromBuyAt != null) {
            criteria.and("buy_at").gte(fromBuyAt);
        }

        if (toBuyAt != null) {
            criteria.and("buy_at").lte(toBuyAt);
        }

        if (fromGuaranteeAt != null) {
            criteria.and("guarantee_expire_at").gte(fromGuaranteeAt);
        }

        if (toGuaranteeAt != null) {
            criteria.and("guarantee_expire_at").lte(toGuaranteeAt);
        }

        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, Equipment.class);

        query.with(pageable);
        query.fields().exclude("user_id", "group_id");
        List<Equipment> content = mongoTemplate.find(query, Equipment.class);

        return new PageImpl<>(content, pageable, total);
    }

}
