package four.group.jahadi.DTO.Region;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.Validator.ValidatedRegionRunInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedRegionRunInfo
public class RegionRunInfoData {

    private String cityId;
    @JsonDeserialize(using = ConvertStringToLongDeserialization.class)
    private Object startAt;
    @JsonDeserialize(using = ConvertStringToLongDeserialization.class)
    private Object endAt;

    private String dailyStartAt;
    private String dailyEndAt;

    private Double lat;
    private Double lng;

}
