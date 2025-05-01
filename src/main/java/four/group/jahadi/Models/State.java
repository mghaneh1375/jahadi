package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "state")
public class State extends Model {

    private String name;

    @Field("country_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId countryId;


    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"name\":" + printNullableField(name) +
                ", \"countryId\":" + printNullableField(countryId) +
                "}\n";
    }

}
