package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.Area.Area;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trip")
@Builder
public class Trip extends Model {

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer no;

    @Builder.Default
    private List<Area> areas = List.of();

    @Field("project_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId projectId;

    @Field("start_at")
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date startAt;

    @Field("end_at")
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date endAt;

    @Field("daily_start_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dailyStartAt;

    @Field("daily_end_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dailyEndAt;

    @Field("groups_with_access")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<GroupAccess> groupsWithAccess;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String project;

}
