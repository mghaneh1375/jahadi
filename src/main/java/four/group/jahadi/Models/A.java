package four.group.jahadi.Models;

import four.group.jahadi.Enums.Module.AnswerType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class A {
    private AnswerType answerType;
    private List<Object> options;
    @Builder.Default
    private Boolean canWriteDesc = false;
}
