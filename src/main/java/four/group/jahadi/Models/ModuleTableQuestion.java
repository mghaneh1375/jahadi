package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Enums.AnswerType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleTableQuestion extends Question {

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

    private AnswerType answerType;
}
