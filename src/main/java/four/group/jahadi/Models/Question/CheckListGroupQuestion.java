package four.group.jahadi.Models.Question;

import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Utility.PairValue;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckListGroupQuestion extends Question {

    @Builder.Default
    private QuestionType questionType = QuestionType.CHECK_LIST;

    @Field("section_title")
    private String sectionTitle;

    private List<PairValue> options;
    private List<Question> questions;
}
