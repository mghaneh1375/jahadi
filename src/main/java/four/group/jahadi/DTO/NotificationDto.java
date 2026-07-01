package four.group.jahadi.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;
    private String areaName;
    private String title;
    private String description;
    private Boolean seenStatus;
}