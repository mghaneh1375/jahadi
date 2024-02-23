package four.group.jahadi.DTO.Area;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.ColorDeserialization;
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
    @JsonDeserialize(using = ColorDeserialization.class)
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
