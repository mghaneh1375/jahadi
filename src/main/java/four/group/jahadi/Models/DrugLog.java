package four.group.jahadi.Models;

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

    @Field("drug_id")
    private ObjectId drugId;

    private Integer amount;
    private String desc;
}
