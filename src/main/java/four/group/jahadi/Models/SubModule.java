package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.Question.Question;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.List;

import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubModule {

    @Id
    @MongoId
    @Field("_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Question> questions;

    @Field("post_action")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String postAction;

    @Field("is_referral")
    @Builder.Default
    private boolean isReferral = false;

    @Field("refer_to")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ObjectId referTo;

    @Field("readonly_module_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId readOnlyModuleId;

    @Field("readonly_sub_module_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId readOnlySubModuleId;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean hasPatientForm;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(id) +
                ", \"name\":" + printNullableField(name) +
                ", \"questions\":" + questions +
                ", \"postAction\":" + printNullableField(postAction) +
                ", \"isReferral\":" + isReferral +
                ", \"referTo\":" + printNullableField(referTo) +
                ", \"readOnlyModuleId\":" + printNullableField(readOnlyModuleId) +
                ", \"readOnlySubModuleId\":" + printNullableField(readOnlySubModuleId) +
                '}';
    }
}
