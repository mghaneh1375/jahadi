package four.group.jahadi.Models.Question.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.Question.Question;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.Question.deserializer.SimpleQuestionDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class QuestionDeserializer extends JsonDeserializer<Question> {
    @Override
    public Question deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode node = mapper.readTree(jp);

        String questionType = node.get("questionType").asText();
        Class<? extends Question> targetClass;

        if ("SIMPLE".equals(questionType))
            return SimpleQuestionDeserializer.deserialize(node, QuestionType.SIMPLE);
        if ("CHECK_LIST".equals(questionType)) {
            if(node.has("questions"))
                return CheckListGroupQuestionDeserializer.deserialize(node);

            return SimpleQuestionDeserializer.deserialize(node, QuestionType.CHECK_LIST);
        }
        if ("GROUP".equals(questionType)) {
            return GroupQuestionDeserializer.deserialize(node);
        }
        if ("TABLE".equals(questionType))
            return TableQuestionDeserializer.deserialize(node);

        throw new IllegalArgumentException("Unknown subtype for questionType=" + questionType);
    }
}
