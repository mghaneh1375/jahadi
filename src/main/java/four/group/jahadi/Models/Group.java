package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Enums.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "group")
public class Group extends ModelWithUser {

    @Indexed(unique=true)
    private String name;

    private Color color;

    @Field("is_active")
    private boolean isActive = true;

}
