package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import lombok.*;


import java.time.LocalDateTime;

import static four.group.jahadi.Utility.Utility.printNullableDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaDates {

    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime start;

    @JsonSerialize(using = DateSerialization.class)
    private LocalDateTime end;

    @Override
    public String toString() {
        return "{" +
                "\"start\":" + printNullableDate(start) +
                ", \"end\":" + printNullableDate(end) +
                "}";
    }
}
