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
public class PatientStats {
    private Long total;
    private Long male;
    private Long female;
    private Long child;
    private Long adult;
    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime refreshAt;
}