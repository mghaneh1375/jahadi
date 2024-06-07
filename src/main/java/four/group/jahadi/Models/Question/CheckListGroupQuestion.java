package four.group.jahadi.Models.Question;

import four.group.jahadi.Utility.PairValue;
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
public class CheckListGroupQuestion extends Question {
    @Field("section_title")
    private String sectionTitle;
    private List<PairValue> options;
    private List<SimpleQuestion> questions;
}
