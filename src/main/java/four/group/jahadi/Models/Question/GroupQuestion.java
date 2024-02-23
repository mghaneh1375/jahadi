package four.group.jahadi.Models.Question;

import four.group.jahadi.Enums.Module.QuestionType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupQuestion extends Question {

    @Builder.Default
    private QuestionType questionType = QuestionType.GROUP;

    @Field("section_title")
    private String sectionTitle;

    private List<Question> questions;
}
