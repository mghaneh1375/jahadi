package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.DateSerialization;
import lombok.*;

import java.util.Date;

import static four.group.jahadi.Utility.Utility.printNullableDate;

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

    @Override
    public String toString() {
        return "{" +
                "\"start\":" + printNullableDate(start) +
                ", \"end\":" + printNullableDate(end) +
                "}";
    }
}
