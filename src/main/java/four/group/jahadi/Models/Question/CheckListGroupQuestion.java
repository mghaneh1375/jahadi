package four.group.jahadi.Models.Question;

import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Utility.PairValue;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class CheckListGroupQuestion extends Question {

    @Field("section_title")
    private String sectionTitle;

    private List<PairValue> options;
    private List<Question> questions;
}
