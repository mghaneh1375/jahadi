package four.group.jahadi.Models.Area;


import four.group.jahadi.Models.Model;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import static four.group.jahadi.Utility.Utility.printNullableDate;
import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperimentInArea extends Model {

    private String title;

    @Field("experiment_id")
    private ObjectId experimentId;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                ", \"title\":" + printNullableField(title) +
                ", \"experimentId\":" + printNullableField(experimentId) +
                '}';
    }
}
