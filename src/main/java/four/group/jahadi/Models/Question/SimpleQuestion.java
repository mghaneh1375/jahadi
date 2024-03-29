package four.group.jahadi.Models.Question;

import four.group.jahadi.Enums.Module.AnswerType;
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
public class SimpleQuestion extends Question {

    @Builder.Default
    private QuestionType questionType = QuestionType.SIMPLE;

    private Boolean required;

    private String question;

    @Field("answer_type")
    private AnswerType answerType;

    private List<PairValue> options;

    @Field("dynamic_options")
    private String dynamicOptions;
}
