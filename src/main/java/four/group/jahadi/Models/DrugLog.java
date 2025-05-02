package four.group.jahadi.Models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static four.group.jahadi.Utility.Utility.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "drug_logs")
@Builder
public class DrugLog extends Model {

    @Field("drug_id")
    private ObjectId drugId;
    @Field("user_id")
    private ObjectId userId;
    @Field("area_id")
    private ObjectId areaId;
    private Integer amount;
    private String desc;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                ", \"drugId\":" + printNullableField(drugId) +
                ", \"userId\":" + printNullableField(userId) +
                ", \"areaId\":" + printNullableField(areaId) +
                ", \"amount\":" + printNullableInteger(amount) +
                ", \"desc\":" + printNullableField(desc) +
                "}\n";
    }
}
