package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class City extends Model {

    private String name;

    @Field("state_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId stateId;

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + this.getId() +
                "\", \"name\":\"" + name +
                "\", \"stateId\":\"" + stateId +
                "\"}\n";
    }
}
