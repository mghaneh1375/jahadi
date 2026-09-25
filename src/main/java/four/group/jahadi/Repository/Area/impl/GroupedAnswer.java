package four.group.jahadi.Repository.Area.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupedAnswer {
    @Field("_id")
    private ObjectId questionId;

    @Field("answers")
    private List<String> answers;
}