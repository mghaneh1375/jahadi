package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPresenceList {

    private List<Dates> dates;
    private User user;

    @Data
    @Builder
    public static class Dates {

        @JsonSerialize(using = ObjectIdSerialization.class)
        private ObjectId id;

        @JsonSerialize(using = DateSerialization.class)
        LocalDateTime entrance;

        @JsonSerialize(using = DateSerialization.class)
        LocalDateTime exit;
    }

    @Field("created_at")
    @CreatedDate
    @JsonSerialize(using = DateSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime createdAt;
}
