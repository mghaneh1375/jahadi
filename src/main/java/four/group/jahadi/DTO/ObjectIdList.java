package four.group.jahadi.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectIdList {

    @NotNull
    @Size(min = 1, max = 100000)
    @JsonDeserialize(using = ObjectIdListDeserializer.class)
    List<ObjectId> ids;
}
