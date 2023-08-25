package four.group.jahadi.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Getter
public abstract class Model implements Serializable {

    @Id
    @MongoId
    @Field("_id")
    @JsonIgnore
    private ObjectId _id;

    @Field("created_at")
    @CreatedDate
    private Date createdAt;
}
