package four.group.jahadi.Models.Question;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Utility.PairValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document
@SuperBuilder
public class SimpleQuestion extends Question {

    private Boolean required;
    private String question;

    @Field("answer_type")
    private AnswerType answerType;

    private List<PairValue> options;

    @Field("dynamic_options")
    private String dynamicOptions;
}
