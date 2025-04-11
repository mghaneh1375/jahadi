package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.Model;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "equipments_in_area")
@Builder
public class AreaEquipments extends Model {

    @Field("equipment_name")
    private String equipmentName;

    @Field("equipment_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId equipmentId;

    @Field("area_id")
    @JsonIgnore
    private ObjectId areaId;

    @Field("updated_at")
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date updatedAt;

    @Field("total_count")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer totalCount;

    private Integer reminder;

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + this.getId() +
                "\", \"createdAt\":\"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(this.getCreatedAt()) +
                "\", \"equipmentName\":\"" + equipmentName +
                "\", \"equipmentId\":\"" + equipmentId +
                "\", \"areaId\":\"" + areaId +
                "\", \"updatedAt\":\"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(updatedAt) +
                "\", \"totalCount\":" + totalCount +
                ", \"reminder\":" + reminder +
                "}\n";
    }
}
