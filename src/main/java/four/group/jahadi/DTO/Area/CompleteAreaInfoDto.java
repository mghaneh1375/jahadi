package four.group.jahadi.DTO.Area;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.CoOrg;
import four.group.jahadi.Models.JustDateSerialization;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CompleteAreaInfoDto {
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId cityId;
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId countryId;
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId stateId;
    @JsonSerialize(using = JustDateSerialization.class)
    private LocalDateTime startAt;
    @JsonSerialize(using = JustDateSerialization.class)
    private LocalDateTime endAt;
    private String dailyStartAt;
    private String dailyEndAt;
    private Double lat;
    private Double lng;
    List<UserAccess> userAccesses;
    private String serialPrefix;
    private CoOrg coOrg;
}
