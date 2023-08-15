package four.group.jahadi.Models;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {

    @Id
    @MongoId
    @Field("_id")
    private ObjectId _id;

    private String firstName;
    private String lastName;
    private String password;
    private Sex sex;
    private String NID;
    private String phone;
    private String educationalField;
    private String specification;
    private String birthDay;
    private List<Access> accesses;
    private List<String> notifs;
    private ObjectId groupId;
    private AccountStatus status;
}
