package four.group.jahadi.Models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPresenceList extends Model {

    private List<Dates> dates;
    private User user;

    @Data
    @Builder
    public static class Dates {
        @JsonSerialize(using = DateSerialization.class)
        private Date entrance;

        @JsonSerialize(using = DateSerialization.class)
        private Date exit;
    }
}
