package four.group.jahadi.Models.Question.deserializer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.TableQuestion;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class TableQuestionDeserializer {
    static TableQuestion deserialize(ObjectNode node) {
        List<String> firstColumn = node.has("firstColumn")
                ? new ArrayList<>()
                : null;
        if(node.has("firstColumn"))
            node.get("firstColumn").forEach(jsonNode -> {
                firstColumn.add(jsonNode.toString());
            });

        List<String> headers = node.has("headers")
                ? new ArrayList<>()
                : null;
        if(node.has("headers"))
            node.get("headers").forEach(jsonNode -> {
                headers.add(jsonNode.toString());
            });

        return TableQuestion
                .builder()
                .id(new ObjectId(node.get("id").asText()))
                .questionType(QuestionType.TABLE)
                .required(node.get("required").asBoolean())
                .answerType(AnswerType.valueOf(node.get("answerType").asText()))
                .title(node.get("title").asText())
                .cellLabel(node.get("cellLabel").asText())
                .rowsCount(node.get("rowsCount").asInt())
                .firstColumn(firstColumn)
                .headers(headers)
                .build();
    }
}
