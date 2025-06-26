package four.group.jahadi.Models.Question.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.CheckListGroupQuestion;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Utility.PairValue;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CheckListGroupQuestionDeserializer {
    static CheckListGroupQuestion deserialize(ObjectNode node) {
        List<SimpleQuestion> questions = new ArrayList<>();
        node.get("questions").forEach(jsonNode -> {
            questions.add(SimpleQuestionDeserializer.deserialize((ObjectNode) jsonNode, QuestionType.SIMPLE));
        });

        List<PairValue> options = new ArrayList<>();
        node.get("options").forEach(jsonNode -> {
            options.add(new PairValue(jsonNode.get("key").asText(), jsonNode.get("value").asText()));
        });

        HashMap<String, Integer> marks = node.has("marks")
                ? new HashMap<>()
                : null;
        if(node.has("marks")) {
            ObjectNode objectNode = (ObjectNode) node.get("marks");
            Iterator<String> fieldNames = objectNode.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                marks.put(key, objectNode.get(key).asInt());
            }
        }

        return CheckListGroupQuestion
                .builder()
                .questions(questions)
                .options(options)
                .id(new ObjectId(node.get("id").asText()))
                .canWriteDesc(node.get("canWriteDesc").asBoolean())
                .canWriteTime(node.get("canWriteTime").asBoolean())
                .canUploadFile(node.get("canUploadFile").asBoolean())
                .canWriteReason(node.get("canWriteReason").asBoolean())
                .canWriteReport(node.get("canWriteReport").asBoolean())
                .canWriteSampleInfoDesc(node.get("canWriteSampleInfoDesc").asBoolean())
                .markable(node.get("markable").asBoolean())
                .questionType(QuestionType.CHECK_LIST)
                .sectionTitle(node.get("sectionTitle").asText())
                .marks(marks)
                .build();
    }
}
