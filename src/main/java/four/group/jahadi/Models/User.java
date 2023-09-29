package four.group.jahadi.Models;

import four.group.jahadi.Enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
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
    private String fatherName;

    @Field("birth_day")
    private String birthDay;

    @Field("university_year")
    private Integer universityYear;

    private String field;
    private String university;

    @Field("NID")
    private String nid;

    private String phone;
    private Sex sex;
    private String abilities;
    private String diseases;
    private String allergies;

    @Field("blood_type")
    private BloodType bloodType;

    @Field("nearby_name")
    private String nearbyName;

    @Field("nearby_phone")
    private String nearbyPhone;

    private Color color;
    private String pic;

    private List<Access> accesses;
    private List<String> notifs;
    private AccountStatus status;


    @Field("group_name")
    private String groupName;

    @Field("organization_dependency")
    private String organizationDependency;

    private String trips;
    private Integer members;

    @Field("familiar_with")
    private String familiarWith;

    @Field("remove_at")
    private Long removeAt;


    @Field("group_id")
    private ObjectId groupId;
}
