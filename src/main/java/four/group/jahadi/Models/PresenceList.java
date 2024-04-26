package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Transient;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "presence_list")
public class PresenceList extends Model {

    @JsonIgnore
    @Field("area_id")
    private ObjectId areaId;

    @Field("user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId userId;

    @JsonSerialize(using = DateSerialization.class)
    private Date entrance;

    @JsonSerialize(using = DateSerialization.class)
    private Date exit;
}
