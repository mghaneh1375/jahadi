package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ware_house_access_for_group")
@Builder
public class ExternalReferralAccessForGroup extends Model {
    @Field("group_id")
    @JsonIgnore
    private ObjectId groupId;
    @Field("user_id")
    @JsonIgnore
    private ObjectId userId;
}
