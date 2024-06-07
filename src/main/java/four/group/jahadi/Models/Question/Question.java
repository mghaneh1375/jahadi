package four.group.jahadi.Models.Question;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.io.Serializable;

@Getter
@SuperBuilder
public class Question implements Serializable {

    @Id
    @MongoId
    @Field("_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;

    private QuestionType questionType;
}
