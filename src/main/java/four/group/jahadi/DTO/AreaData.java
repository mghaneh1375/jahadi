package four.group.jahadi.DTO;

import four.group.jahadi.Enums.Color;
import four.group.jahadi.Models.Area;
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
    private Color color;

    public Area convertToArea() {
        return Area.builder()
                .id(new ObjectId())
                .ownerId(owner)
                .color(color)
                .name(name)
                .build();
    }

}
