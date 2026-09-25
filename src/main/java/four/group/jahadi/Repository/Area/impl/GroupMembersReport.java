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
public class GroupMembersReport {
    private Integer members;
    private GroupStatisticsResult groupStatisticsResult;
    private Long menMembers;
    private Long womenMembers;
    private Long membersInTrip;
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime refreshAt;
}
