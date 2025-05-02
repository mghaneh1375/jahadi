package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.Model;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.time.LocalDateTime;

import static four.group.jahadi.Utility.Utility.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "drugs_in_area")
@Builder
public class AreaDrugs extends Model {

    @Field("drug_name")
    private String drugName;

    @Field("drug_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId drugId;

    @Field("area_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ObjectId areaId;

    @Field("updated_at")
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updatedAt;

    @Field("total_count")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer totalCount;
    private Integer reminder;

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                ", \"drugName\":" + printNullableField(drugName) +
                ", \"drugId\":" + printNullableField(drugId) +
                ", \"areaId\":" + printNullableField(areaId) +
                ", \"updatedAt\":" + printNullableDate(updatedAt) +
                ", \"totalCount\":" + printNullableInteger(totalCount) +
                ", \"reminder\":" + printNullableInteger(reminder) +
                "}\n";
    }
}
