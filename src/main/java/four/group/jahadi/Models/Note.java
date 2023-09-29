package four.group.jahadi.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "note")
public class Note {

    @Id
    @MongoId
    @Field("_id")
    private ObjectId _id;

    @Field("created_at")
    @CreatedDate
    private Date createdAt;

    @Field("updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    private String title;
    private String description;

    @Field("user_id")
    private ObjectId userId;

}
