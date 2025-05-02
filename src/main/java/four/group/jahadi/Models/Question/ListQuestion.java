package four.group.jahadi.Models.Question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@Setter
@Document
@NoArgsConstructor
@SuperBuilder
public class ListQuestion extends Question {
    private List<Question> questions;

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"questionType\":" + printNullableField(this.getQuestionType()) +
                ", \"questions\":" + questions +
                '}';
    }
}
