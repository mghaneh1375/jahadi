package four.group.jahadi.Models.Question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static four.group.jahadi.Utility.Utility.*;

@Getter
@Setter
@Document
@NoArgsConstructor
@SuperBuilder
public class GroupQuestion extends Question {
    @Field("section_title")
    private String sectionTitle;
    private List<Question> questions;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"questionType\":" + printNullableField(this.getQuestionType()) +
                ", \"sectionTitle\":" + printNullableField(sectionTitle) +
                ", \"questions\":" + questions +
                '}';
    }
}
