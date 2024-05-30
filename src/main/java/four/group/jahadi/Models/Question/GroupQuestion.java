package four.group.jahadi.Models.Question;

import four.group.jahadi.Enums.Module.QuestionType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class GroupQuestion extends Question {

    @Field("section_title")
    private String sectionTitle;

    private List<Question> questions;
}
