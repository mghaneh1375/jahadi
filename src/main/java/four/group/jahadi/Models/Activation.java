package four.group.jahadi.Models;

import lombok.*;
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
@Builder
public class Activation {

    @Id
    @MongoId
    @Field("_id")
    private ObjectId _id;

    private User user;
    private String token;
    private String phone;
    private String nid;
    private Integer code;
    private Boolean validated;

    @Field("created_at")
    private Long createdAt;
}
