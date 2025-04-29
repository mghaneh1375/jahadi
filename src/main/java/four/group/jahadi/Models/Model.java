package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public abstract class Model implements Serializable {

    @Id
    @MongoId
    @Field("_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;

    @Field("created_at")
    @CreatedDate
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdAt;

    public Model createId() {
        this.id = new ObjectId();
        this.createdAt = LocalDateTime.now();
        return this;
    }

    public void setId(ObjectId oid) {
        this.id = oid;
    }
}
