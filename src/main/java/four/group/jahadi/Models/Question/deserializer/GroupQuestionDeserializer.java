package four.group.jahadi.Models.Question.deserializer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.GroupQuestion;
import four.group.jahadi.Models.Question.Question;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class GroupQuestionDeserializer {
    static GroupQuestion deserialize(ObjectNode node) {
        List<Question> questions = new ArrayList<>();
        node.get("questions").forEach(jsonNode -> {
            switch (jsonNode.get("questionType").asText()) {
                case "TABLE":
                    questions.add(TableQuestionDeserializer.deserialize((ObjectNode) jsonNode));
                    break;
                case "SIMPLE":
                    questions.add(SimpleQuestionDeserializer.deserialize((ObjectNode) jsonNode, QuestionType.SIMPLE));
                    break;
                case "CHECK_LIST":
                    questions.add(SimpleQuestionDeserializer.deserialize((ObjectNode) jsonNode, QuestionType.CHECK_LIST));
                    break;
            }
        });
        return GroupQuestion
                .builder()
                .questions(questions)
                .id(new ObjectId(node.get("id").asText()))
                .questionType(QuestionType.GROUP)
                .sectionTitle(node.get("sectionTitle").asText())
                .build();
    }
}
