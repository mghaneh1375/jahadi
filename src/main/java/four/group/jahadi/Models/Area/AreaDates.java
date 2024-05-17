package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaDates {

    @JsonSerialize(using = DateSerialization.class)
    private Date start;

    @JsonSerialize(using = DateSerialization.class)
    private Date end;
}
