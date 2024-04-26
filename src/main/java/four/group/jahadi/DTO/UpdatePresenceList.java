package four.group.jahadi.DTO;

import four.group.jahadi.Validator.ValidatedUpdatePresenceList;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ValidatedUpdatePresenceList
public class UpdatePresenceList {
    private Date entrance;
    private Date exit;
    private Boolean justSetExit;
}
