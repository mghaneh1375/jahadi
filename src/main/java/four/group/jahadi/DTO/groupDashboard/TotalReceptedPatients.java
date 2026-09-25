package four.group.jahadi.DTO.groupDashboard;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalReceptedPatients {
    private Long data;
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime refreshAt;
}
