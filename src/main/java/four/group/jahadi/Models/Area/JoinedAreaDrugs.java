package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Models.Drug;
import four.group.jahadi.Models.Model;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinedAreaDrugs extends Model {

    @Field("updated_at")
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date updatedAt;

    @Field("total_count")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer totalCount;

    private Integer reminder;
    private Drug drugInfo;
}
