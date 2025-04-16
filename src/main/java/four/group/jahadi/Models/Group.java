package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Models.Area.Area;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "group")
@Builder
public class Group extends ModelWithUser {

    @Indexed(unique=true)
    private String name;

    @Builder.Default
    @JsonSerialize(using = ColorSerialization.class)
    private Color color = Color.BLUE;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = PicSerialization.class)
    private String pic;

    @Field("is_active")
    @Builder.Default
    private boolean isActive = true;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private List<Area> areas;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer tripsCount;

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name +
                "\", \"color\":\"" + color +
                "\", \"code\":" + code +
                ", \"pic\":\"" + pic +
                "\", \"isActive\":" + isActive +
                ", \"id\":\"" + this.getId() +
                "\", \"createdAt\":\"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(this.getCreatedAt()) +
                "\", \"owner\":\"" + this.getOwner() +
                "\"}\n";
    }
}
