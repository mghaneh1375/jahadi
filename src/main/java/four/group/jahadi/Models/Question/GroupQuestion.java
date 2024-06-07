package four.group.jahadi.Models.Question;

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
public class GroupQuestion extends Question {

    @Field("section_title")
    private String sectionTitle;

    private List<Question> questions;
}
