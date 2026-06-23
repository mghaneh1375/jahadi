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

import java.util.ArrayList;
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
            Boolean justName, Criteria... moreCriteriaList
    ) {
        List<Criteria> criteriaList = new ArrayList<>();

        criteriaList.add(
                new Criteria().orOperator(
                        Criteria.where("remove_at").is(null),
                        Criteria.where("remove_at").exists(false)
                )
        );

        if (status != null) {
            criteriaList.add(Criteria.where("status").is(status));
        }

        if (access != null) {
            criteriaList.add(Criteria.where("accesses").is(access));
        }

        if (name != null && !name.isEmpty()) {
            criteriaList.add(Criteria.where("name").regex(name, "i"));
        }

        if (NID != null) {
            criteriaList.add(Criteria.where("NID").regex(NID, "i"));
        }

        if (phone != null) {
            criteriaList.add(Criteria.where("phone").regex(phone, "i"));
        }

        if (sex != null) {
            criteriaList.add(Criteria.where("sex").is(sex));
        }

        if (groupName != null) {
            criteriaList.add(Criteria.where("group_name").regex(groupName, "i"));
        }

        if (groupId != null) {
            criteriaList.add(Criteria.where("group_id").is(groupId));
        }

        if (Boolean.TRUE.equals(justGroupRequests)) {
            criteriaList.add(Criteria.where("total_members").exists(true));
        }

        if (moreCriteriaList != null && moreCriteriaList.length > 0) {
            criteriaList.add(new Criteria().andOperator(moreCriteriaList));
        }

        if (searchKey != null && !searchKey.isEmpty()) {
            criteriaList.add(
                    new Criteria().orOperator(
                            Criteria.where("group_name").regex(searchKey, "i"),
                            Criteria.where("NID").regex(searchKey, "i"),
                            Criteria.where("name").regex(searchKey, "i"),
                            Criteria.where("phone").regex(searchKey, "i"),
                            Criteria.where("father_name").regex(searchKey, "i"),
                            Criteria.where("university").regex(searchKey, "i")
                    )
            );
        }

        Query query = new Query(new Criteria()
                .andOperator(criteriaList.toArray(new Criteria[0]))
        );

        if(Boolean.TRUE.equals(justName))
            query.fields().include("name").include("_id");

        long total = mongoTemplate.count(query, User.class);
        query.with(pageable);
        List<User> content = mongoTemplate.find(query, User.class);

        return new PageImpl<>(content, pageable, total);
    }

}
