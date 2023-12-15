package four.group.jahadi.DTO.Region;

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

    private Long startAt;
    private Long endAt;

    private String dailyStartAt;
    private String dailyEndAt;

    private Double lat;
    private Double lng;

}
