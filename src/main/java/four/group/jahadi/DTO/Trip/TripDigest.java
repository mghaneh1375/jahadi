package four.group.jahadi.DTO.Trip;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.DTO.Area.AreaDigest;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripDigest {
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;
    private String name;
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime startAt;
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime endAt;
    private String dailyStartAt;
    private String dailyEndAt;
    private List<AreaDigest> areas;
}
