package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Enums.Color;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
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
    private Color color = Color.BLUE;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer code;

    @Field("is_active")
    @Builder.Default
    private boolean isActive = true;

}
