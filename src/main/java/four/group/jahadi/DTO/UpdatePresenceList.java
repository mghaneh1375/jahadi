package four.group.jahadi.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import four.group.jahadi.Validator.ValidatedUpdatePresenceList;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ValidatedUpdatePresenceList
public class UpdatePresenceList {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime entrance;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime exit;
    private Boolean justSetExit;
}
