package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Models.*;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId id;

    @Field("owner_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId ownerId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ColorSerialization.class)
    private Color color;

    @NonNull
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String country;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String state;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String city;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId stateId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId cityId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double lat;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double lng;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdListSerialization.class)
    @Builder.Default
    private List<ObjectId> members = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder.Default
    private List<ExperimentInArea> experiments = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder.Default
    private List<ModuleInArea> modules = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdListSerialization.class)
    @Builder.Default
    private List<ObjectId> dispatchers = new ArrayList<>();

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private User owner;

    @JsonIgnore
    @Builder.Default
    private Boolean finished = false;

}
