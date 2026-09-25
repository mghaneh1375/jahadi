package four.group.jahadi.Repository.Area.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateTripCount {
    private String state;
    private Integer count;
}