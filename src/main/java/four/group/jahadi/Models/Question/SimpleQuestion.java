package four.group.jahadi.Models.Question;

import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Utility.PairValue;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

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

    @Field("dynamic_options")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dynamicOptions;

    @Builder.Default
    @Field("can_write_desc")
    private Boolean canWriteDesc = false;
}
