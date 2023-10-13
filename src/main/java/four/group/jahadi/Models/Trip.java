package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trip")
@Builder
public class Trip extends ModelWithUser {

    private String name;
    private Integer no;
    private List<Area> areas = new ArrayList();

    @Field("project_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId projectId;

    @Field("start_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date startAt;

    @Field("end_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date endAt;

    @Field("daily_start_at")
    private String dailyStartAt;

    @Field("daily_end_at")
    private String dailyEndAt;

}
