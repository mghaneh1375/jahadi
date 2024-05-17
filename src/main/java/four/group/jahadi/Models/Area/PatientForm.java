package four.group.jahadi.Models.Area;

import four.group.jahadi.Models.Model;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientForm extends Model {

    @Field("sub_module_id")
    private ObjectId subModuleId;


}
