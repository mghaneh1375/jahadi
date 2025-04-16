package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

import static four.group.jahadi.Utility.Utility.printNullableDate;
import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "note")
public class Note extends Model {

    @Field("updated_at")
    @UpdateTimestamp
    @JsonSerialize(using = DateSerialization.class)
    private Date updatedAt;

    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @Field("user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId userId;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                ", \"updatedAt\":" + printNullableDate(updatedAt) +
                ", \"title\":" + printNullableField(title) +
                ", \"description\":" + printNullableField(description) +
                ", \"userId\":" + printNullableField(userId) +
                "}\n";
    }
}
