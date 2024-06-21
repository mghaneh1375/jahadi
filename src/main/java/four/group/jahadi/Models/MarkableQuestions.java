package four.group.jahadi.Models;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.HashMap;

@Data
@Builder
public class MarkableQuestions {
    private HashMap<String, Integer> marks;
    private ObjectId questionId;
    private ObjectId parentId;
}
