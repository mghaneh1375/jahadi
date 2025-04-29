package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.Equipment;
import four.group.jahadi.Models.Model;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinedAreaEquipments extends Model {

    @Field("updated_at")
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updatedAt;

    @Field("total_count")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer totalCount;

    private Integer reminder;
    private Equipment equipmentInfo;
}
