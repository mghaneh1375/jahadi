package four.group.jahadi.Repository.Area.impl;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupStatistics {
    private Long tripCount;
    private Long areaCount;
    private Long stateCount;
    private Long cityCount;
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime refreshAt;
}