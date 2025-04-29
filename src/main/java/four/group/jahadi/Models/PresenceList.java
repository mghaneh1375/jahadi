package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "presence_list")
public class PresenceList extends Model {

    @Field("area_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ObjectId areaId;

    @Field("user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId userId;

    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime entrance;

    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime exit;

    @Override
    public String toString() {
        return "{" +
                "\"areaId\":\"" + areaId +
                "\", \"userId\":\"" + userId +
                "\", \"entrance\":" + entrance.toString() +
                "\", \"exit\":" + exit.toString() +
                "}\n";
    }
}
