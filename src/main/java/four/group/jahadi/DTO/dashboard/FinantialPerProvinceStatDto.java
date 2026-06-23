package four.group.jahadi.DTO.dashboard;

import four.group.jahadi.Models.ReportThreshold;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinantialPerProvinceStatDto {
    private List<FinantialPerProvinceData> report;
    private ReportThreshold thresholds;
}
