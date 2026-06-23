package four.group.jahadi.DTO.Area;

import four.group.jahadi.Validator.ValidatedUpdateArea;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ValidatedUpdateArea
public class UpdateAreaData {
    private ObjectId areaId;
    @NotBlank
    private String name;
    @NonNull
    private ObjectId owner;
}
