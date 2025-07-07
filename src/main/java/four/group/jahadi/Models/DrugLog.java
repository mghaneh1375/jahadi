package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "drug_logs")
@Builder
public class DrugLog extends Model {
    @Field("group_id")
    @JsonIgnore
    private ObjectId groupId;
    @Field("drug_id")
    private ObjectId drugId;
    @Field("user_id")
    private ObjectId userId;
    @Field("area_id")
    private ObjectId areaId;
    private Integer amount;
    private String desc;
}
