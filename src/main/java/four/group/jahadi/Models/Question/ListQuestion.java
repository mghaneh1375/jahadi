package four.group.jahadi.Models.Question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
@NoArgsConstructor
@SuperBuilder
public class ListQuestion extends Question {
    private List<Question> questions;
}
