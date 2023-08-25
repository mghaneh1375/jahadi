package four.group.jahadi.Models;

import four.group.jahadi.Enums.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("project")
public class Project extends Model implements Serializable {

    @Indexed(unique = true)
    private String name;

    private Color color;
    private int progress = 0;

    @Field("group_ids")
    @JsonIgnore
    private List<ObjectId> groupIds;

    @Field("trip_ids")
    @JsonIgnore
    private List<ObjectId> tripIds;

    @Field("start_at")
    private Integer startAt;

    @Field("end_at")
    private Integer endAt;

}
