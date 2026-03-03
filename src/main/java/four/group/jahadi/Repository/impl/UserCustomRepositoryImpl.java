package four.group.jahadi.Repository.impl;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Models.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCustomRepositoryImpl {

    private final MongoTemplate mongoTemplate;

    public Page<User> findAdvanced(
            AccountStatus status, Access access, String name,
            String NID, String phone, Sex sex, String groupName,
            ObjectId groupId, Boolean justGroupRequests,
            String searchKey, Pageable pageable,
            Criteria ...moreCriteriaList
    ) {
        Criteria criteria = new Criteria();
        criteria.andOperator(
                new Criteria().orOperator(
                        Criteria.where("remove_at").is(null),
                        Criteria.where("remove_at").exists(false)
                )
        );

        if(status != null)
            criteria.and("status").is(status);

        if(access != null)
            criteria.and("access").is(access);

        if (name != null && !name.isEmpty()) {
            criteria.and("name").regex(name, "i");
        }

        if(NID != null)
            criteria.and("NID").regex(NID, "i");

        if(phone != null)
            criteria.and("phone").regex(phone, "i");

        if(sex != null)
            criteria.and("sex").is(sex);

        if(groupName != null)
            criteria.and("group_name").regex(groupName, "i");

        if(groupId != null)
            criteria.and("group_id").is(groupId);

        if(Boolean.TRUE.equals(justGroupRequests))
            criteria.and("total_members").exists(true);

        if(moreCriteriaList != null && moreCriteriaList.length > 0) {
            criteria.andOperator(moreCriteriaList);
        }

        if(searchKey != null) {
            criteria.orOperator(
                    Criteria.where("group_name").regex(searchKey, "i"),
                    Criteria.where("NID").regex(searchKey, "i"),
                    Criteria.where("name").regex(searchKey, "i"),
                    Criteria.where("phone").regex(searchKey, "i"),
                    Criteria.where("father_name").regex(searchKey, "i"),
                    Criteria.where("university").regex(searchKey, "i")
            );
        }

        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, User.class);

        query.with(pageable);
        List<User> content = mongoTemplate.find(query, User.class);

        return new PageImpl<>(content, pageable, total);
    }

}
