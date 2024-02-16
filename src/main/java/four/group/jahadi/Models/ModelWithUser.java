package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;

@Getter
@Setter
public abstract class ModelWithUser extends Model {

    @JsonSerialize(using = ObjectIdSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId owner;

    @Transient
    @JsonView
    @JsonSerialize(using = OwnerSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;

}
