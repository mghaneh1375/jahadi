package four.group.jahadi.Models;

import four.group.jahadi.Enums.AnswerType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleQuestion extends Question {

    private String question;

    @Field("answer_type")
    private AnswerType answerType;

    private List<String> options;
}
