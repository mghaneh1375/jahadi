package four.group.jahadi.DTO.dashboard;

import four.group.jahadi.Models.ReportThreshold;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerProvinceData {
    private String province;
    private Integer count;
}
