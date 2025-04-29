package four.group.jahadi.DTO;

import four.group.jahadi.Validator.ValidatedUpdatePresenceList;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ValidatedUpdatePresenceList
public class UpdatePresenceList {
    private LocalDateTime entrance;
    private LocalDateTime exit;
    private Boolean justSetExit;
}
