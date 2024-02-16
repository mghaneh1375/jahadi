package four.group.jahadi.DTO;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Models.ColorDeserialization;
import four.group.jahadi.Validator.ValidatedGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
@Setter
@ValidatedGroup
public class GroupData {

    ObjectId owner;
    String name;
    
    @JsonDeserialize(using = ColorDeserialization.class)
    Color color;

}
