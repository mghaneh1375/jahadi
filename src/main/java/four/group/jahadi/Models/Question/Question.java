package four.group.jahadi.Models.Question;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.io.Serializable;

import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@NoArgsConstructor
@SuperBuilder
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Determines how type information is added
        include = JsonTypeInfo.As.EXISTING_PROPERTY, // Determines where type information is added
        property = "questionType", // The field in JSON that indicates the type
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GroupQuestion.class, name = "GROUP"),
        @JsonSubTypes.Type(value = CheckListGroupQuestion.class, name = "CHECK_LIST"),
        @JsonSubTypes.Type(value = SimpleQuestion.class, name = "SIMPLE"),
        @JsonSubTypes.Type(value = TableQuestion.class, name = "TABLE"),
})
public abstract class Question implements Serializable {

    @Id
    @MongoId
    @Field("_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;

    @Field("question_type")
    private QuestionType questionType;

    public Question(ObjectId id, QuestionType questionType) {
        this.id = id;
        this.questionType = questionType;
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(id) +
                ", \"questionType\":" + printNullableField(questionType) +
                '}';
    }
}
