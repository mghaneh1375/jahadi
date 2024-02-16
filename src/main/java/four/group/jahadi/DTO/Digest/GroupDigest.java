package four.group.jahadi.DTO.Digest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Models.ColorDeserialization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupDigest extends DTO {

    String name;

    @JsonDeserialize(using = ColorDeserialization.class)
    Color color;

}
