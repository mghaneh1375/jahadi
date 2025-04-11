package four.group.jahadi.Models.Question;

import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Enums.Module.AnswerType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Size;
import java.util.List;

import static four.group.jahadi.Utility.Utility.*;

@Getter
@Setter
@Document
@NoArgsConstructor
@SuperBuilder
public class TableQuestion extends Question {

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

    @Field("answer_type")
    private AnswerType answerType;

    private Boolean required;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"questionType\":" + printNullableField(this.getQuestionType()) +
                ", \"title\":" + printNullableField(title) +
                ", \"cellLabel\":" + printNullableField(cellLabel) +
                ", \"answerType\":" + printNullableField(answerType) +
                ", \"rowsCount\":" + printNullableInteger(rowsCount) +
                ", \"headers\":" + toStringOfList(headers) +
                ", \"firstColumn\":" + toStringOfList(firstColumn) +
                ", \"required\":" + required +
                ", \"rtl\":" + rtl +
                '}';
    }
}
