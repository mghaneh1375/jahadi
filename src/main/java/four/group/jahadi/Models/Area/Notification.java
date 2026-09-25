package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.NotificationType;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.Model;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notification")
public class Notification extends Model {

    @Field("area_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId areaId;


    @Field("user_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId userId;


    private String title;


    private String description;


    private NotificationType type;


    @Field("seen_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(contentUsing = ObjectIdSerialization.class)
    private List<ObjectId> seenBy = new ArrayList<>();


    @Field("updated_at")
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime updatedAt;
}