package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActiveGroups {
    private String name;
    @JsonSerialize(using = PicSerialization.class)
    private String pic;
}
