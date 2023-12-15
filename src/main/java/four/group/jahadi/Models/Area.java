package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Color;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area {

    @Id
    @MongoId
    @Field("_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;

    @Field("owner_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId ownerId;

    private Color color;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String country;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String state;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String city;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double lat;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double lng;

    @Field("start_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date startAt;

    @Field("end_at")
    @JsonSerialize(using = DateSerialization.class)
    private Date endAt;

    @Field("daily_start_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dailyStartAt;

    @Field("daily_end_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dailyEndAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdListSerialization.class)
    private List<ObjectId> members = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ModuleInArea> modules = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdListSerialization.class)
    private List<ObjectId> dispatchers = new ArrayList<>();

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private User owner;

    @JsonIgnore
    private boolean finished = false;

}
