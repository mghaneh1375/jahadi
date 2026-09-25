package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "report_access_for_group")
@Builder
public class ReportAccessForGroup extends Model {
    @Field("group_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ObjectId groupId;
    @Field("user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ObjectId userId;
}
