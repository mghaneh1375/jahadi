package four.group.jahadi.Models;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class A {
    private AnswerType answerType;
    private QuestionType questionType;
    private Integer rows;
    private Integer cols;
    private ObjectId questionId;
    private ObjectId parentId;

    @Builder.Default
    private HashMap<String, Integer> marks = null;

    @Builder.Default
    private List<Object> options = null;

    @Builder.Default
    private Boolean canWriteDesc = false;
    @Builder.Default
    private Boolean canWriteReason = false;
    @Builder.Default
    private Boolean canWriteReport = false;
    @Builder.Default
    private Boolean canWriteTime = false;
    @Builder.Default
    private Boolean canWriteSampleInfoDesc = false;
    @Builder.Default
    private Boolean canUploadFile = false;

    private Boolean required;
}
