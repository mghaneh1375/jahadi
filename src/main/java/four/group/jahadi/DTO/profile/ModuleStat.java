package four.group.jahadi.DTO.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleStat {
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer patients;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer waitingPatients;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer receptedPatients;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer receptedByMe;
}
