package four.group.jahadi.Models.Area;


import four.group.jahadi.Models.Model;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperimentInArea extends Model {

    private String title;

    @Field("experiment_id")
    private ObjectId experimentId;

}
