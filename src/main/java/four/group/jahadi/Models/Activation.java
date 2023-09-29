package four.group.jahadi.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "activation")
public class Activation {

    @Id
    @MongoId
    @Field("_id")
    private ObjectId _id;

    private org.bson.Document user;
    private String token;
    private String phone;
    private String nid;
    private Integer code;
    private Long createdAt;
}
