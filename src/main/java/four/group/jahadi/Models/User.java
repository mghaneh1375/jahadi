package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User extends Model implements Serializable {

    private String name;

    @JsonIgnore
    private String password;

    @Field("father_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fatherName;

    @Field("birth_day")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String birthDay;

    @Field("university_year")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer universityYear;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String university;

    @Field("NID")
    private String nid;

    private String phone;
    private Sex sex;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String abilities;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String diseases;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String allergies;

    @Field("blood_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BloodType bloodType;

    @Field("nearby_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nearbyName;

    @Field("nearby_phone")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nearbyPhone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Color color;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pic;

    private List<Access> accesses;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> notifs;

    private AccountStatus status;

    @Field("group_name")
    private String groupName;

    @Field("organization_dependency")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String organizationDependency;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String trips;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer members;

    @Field("familiar_with")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String familiarWith;

    @Field("remove_at")
    @JsonIgnore
    private Long removeAt;

    @Field("group_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId groupId;
}
