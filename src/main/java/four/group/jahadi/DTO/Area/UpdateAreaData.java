package four.group.jahadi.DTO.Area;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Models.ColorDeserialization;
import four.group.jahadi.Validator.ValidatedArea;
import four.group.jahadi.Validator.ValidatedUpdateArea;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ValidatedUpdateArea
public class UpdateAreaData {
    private ObjectId areaId;
    private String name;
    private ObjectId owner;
    @JsonDeserialize(using = ColorDeserialization.class)
    private Color color;
}
