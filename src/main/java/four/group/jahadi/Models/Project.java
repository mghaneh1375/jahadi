package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Color;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("project")
@Builder
public class Project extends Model {

    @Indexed(unique = true)
    private String name;

    private Color color;
    private int progress = 0;

    @Field("start_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date startAt;

    @Field("end_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date endAt;

    @Field("group_accesses")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private List<GroupAccess> groupAccesses;

}
