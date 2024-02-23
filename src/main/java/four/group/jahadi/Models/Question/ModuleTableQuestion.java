package four.group.jahadi.Models.Question;

import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
public class ModuleTableQuestion extends Question {

    @Builder.Default
    private QuestionType questionType = QuestionType.TABLE;

    private String title;

    @Size(min = 1)
    private List<String> headers;

    @Field("rows_count")
    private Integer rowsCount;

    @Field("first_column")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> firstColumn;

    @Field("cell_label")
    private String cellLabel;

    @Builder.Default
    private boolean rtl = false;

    private AnswerType answerType;
}
