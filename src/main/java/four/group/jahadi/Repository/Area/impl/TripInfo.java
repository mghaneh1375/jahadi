package four.group.jahadi.Repository.Area.impl;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripInfo {
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId areaId;
    private String areaName;
    private String tripName;
}
