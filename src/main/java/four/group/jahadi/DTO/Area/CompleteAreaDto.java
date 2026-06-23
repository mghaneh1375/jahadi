package four.group.jahadi.DTO.Area;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.DTO.Region.ConvertStringToLongDeserialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Data
public class CompleteAreaDto {

    @NotNull
    private ObjectId cityId;

    @NotNull
    @JsonDeserialize(using = ConvertStringToLongDeserialization.class)
    private Object startAt;

    @NotNull
    @JsonDeserialize(using = ConvertStringToLongDeserialization.class)
    private Object endAt;

    @NotNull
    private String dailyStartAt;

    @NotNull
    private String dailyEndAt;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;

    @Valid
    @NotNull
    @Size(min = 1)
    List<UserAccess> userAccesses;

}
