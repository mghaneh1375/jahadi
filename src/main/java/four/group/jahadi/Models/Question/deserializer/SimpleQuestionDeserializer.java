package four.group.jahadi.Models.Question.deserializer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class SimpleQuestionDeserializer {
    static SimpleQuestion deserialize(ObjectNode node, QuestionType questionType) {
        List<PairValue> options = node.has("options")
                ? new ArrayList<>()
                : null;
        if(node.has("options")) {
            node.get("options").forEach(jsonNode -> {
                options.add(new PairValue(jsonNode.get("key").asText(), jsonNode.get("value").asText()));
            });
        }

        return new SimpleQuestion(
                new ObjectId(node.get("id").asText()),
                questionType,
                node.get("required").asBoolean(),
                node.get("question").asText(),
                AnswerType.valueOf(node.get("answerType").asText()),
                options,
                node.get("canWriteDesc").asBoolean()
        );
    }
}
