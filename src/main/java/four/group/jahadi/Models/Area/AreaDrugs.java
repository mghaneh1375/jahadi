package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Models.Model;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    private ObjectId drugId;

    @Field("area_id")
    @JsonIgnore
    private ObjectId areaId;

    @Field("total_count")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer totalCount;

    private Integer reminder;
}
