package four.group.jahadi.DTO.Area;

import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Validator.ValidatedArea;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ValidatedArea
public class AreaData {

    private String name;
    private ObjectId owner;

    public Area convertToArea() {
        return Area.builder()
                .id(new ObjectId())
                .ownerId(owner)
                .name(name)
                .build();
    }

}
