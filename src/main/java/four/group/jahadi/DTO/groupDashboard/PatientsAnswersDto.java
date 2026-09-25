package four.group.jahadi.DTO.groupDashboard;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import four.group.jahadi.Service.dashboard.GroupDashboardService;
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
public class PatientsAnswersDto {
    private List<GroupDashboardService.QuestionDigest> data;
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime refreshTime;
}
