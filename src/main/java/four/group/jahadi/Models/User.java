package four.group.jahadi.Models;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User extends Model implements Serializable {

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @JsonIgnore
    private String password;

    private Color color;
    private String pic;
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
