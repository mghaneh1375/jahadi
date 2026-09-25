package four.group.jahadi.DTO.groupDashboard;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.DTO.dashboard.PerProvinceData;
import four.group.jahadi.Models.DateSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PerProvinceDto {
    private List<PerProvinceData> data;
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime refreshAt;
}
