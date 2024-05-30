package four.group.jahadi.Models.Question;


import four.group.jahadi.Enums.Module.QuestionType;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
public class Question implements Serializable {
    protected QuestionType questionType;
}
