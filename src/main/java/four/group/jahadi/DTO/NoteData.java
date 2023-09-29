package four.group.jahadi.DTO;

import four.group.jahadi.Validator.ValidatedNote;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidatedNote
public class NoteData {

    private String title;
    private String description;

}
