package four.group.jahadi.DTO.Digest;

import four.group.jahadi.Enums.Color;
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
    Color color;

}
