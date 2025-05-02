package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static four.group.jahadi.Utility.Utility.printNullableDate;
import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ware_house_access_for_group")
@Builder
public class WareHouseAccessForGroup extends Model {
    @Field("group_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ObjectId groupId;
    @Field("user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ObjectId userId;
    @Field("has_access_for_drug")
    private Boolean hasAccessForDrug;
    @Field("has_access_for_equipment")
    private Boolean hasAccessForEquipment;


    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"groupId\":" + printNullableField(groupId) +
                ", \"userId\":" + printNullableField(userId) +
                ", \"hasAccessForDrug\":" + (hasAccessForDrug != null && hasAccessForDrug) +
                ", \"hasAccessForEquipment\":" + (hasAccessForEquipment != null && hasAccessForEquipment) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                "}\n";
    }
}
