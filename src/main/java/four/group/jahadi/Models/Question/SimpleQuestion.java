package four.group.jahadi.Models.Question;

import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Utility.PairValue;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static four.group.jahadi.Utility.Utility.printNullableField;
import static four.group.jahadi.Utility.Utility.toStringOfPairValue;

@Getter
@Setter
@Document
@NoArgsConstructor
@SuperBuilder
public class SimpleQuestion extends Question {

    @Builder.Default
    private Boolean required = false;

    private String question;

    @Field("answer_type")
    private AnswerType answerType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PairValue> options;

    @Builder.Default
    @Field("can_write_desc")
    private Boolean canWriteDesc = false;

    public SimpleQuestion(ObjectId id, QuestionType questionType, Boolean required, String question, AnswerType answerType, List<PairValue> options, Boolean canWriteDesc) {
        super(id, questionType);
        this.required = required;
        this.question = question;
        this.answerType = answerType;
        this.options = options;
        this.canWriteDesc = canWriteDesc;
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"questionType\":" + printNullableField(this.getQuestionType()) +
                ", \"required\":" + required +
                ", \"question\":" + printNullableField(question) +
                ", \"answerType\":" + printNullableField(answerType) +
                ", \"options\":" + toStringOfPairValue(options) +
                ", \"canWriteDesc\":" + canWriteDesc +
                '}';
    }
}
