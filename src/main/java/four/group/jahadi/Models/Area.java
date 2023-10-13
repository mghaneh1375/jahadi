package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Color;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area {

    @Id
    @MongoId
    @Field("_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;

    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId ownerId;

    private Color color;
    private String name;

}
