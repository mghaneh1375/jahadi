package four.group.jahadi.Repository.Area.impl;

import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.AreaEquipments;
import four.group.jahadi.Models.Equipment;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class EquipmentsInAreaRepositoryImp {

    @Autowired
    MongoTemplate mongoTemplate;

    public void countDown(ObjectId areaId, ObjectId equipmentId, int count) {
        Query query = new Query();
        query.addCriteria(Criteria.where("area_id").is(areaId));
        query.addCriteria(Criteria.where("equipment_id").is(equipmentId));
        query.addCriteria(Criteria.where("reminder").gte(count));
        Update update = new Update();
        update.inc("reminder", -count);
        update.inc("total_count", -count);
        AreaEquipments andModify = mongoTemplate.findAndModify(query, update, AreaEquipments.class);
        if(andModify == null)
            throw new NotAccessException();

        query = new Query();
        query.addCriteria(Criteria.where("_id").is(equipmentId));
        update = new Update();
        update.inc("available", count);
        mongoTemplate.updateFirst(query, update, Equipment.class);
    }
}
