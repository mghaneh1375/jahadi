package four.group.jahadi.DTO.dashboard;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.ReportThreshold;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinantialPerProvinceData {
    private String province;
    @JsonSerialize(using = NormalizeByMilionSerializer.class)
    private BigDecimal count;
    private ReportThreshold thresholds;
}
