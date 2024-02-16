package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.List;

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

    @Field("can_suggest_drug")
    @Builder.Default
    private boolean canSuggestDrug = false;

    @Field("has_internal_referral")
    @Builder.Default
    private boolean hasInternalReferral = false;

    @Field("has_external_referral")
    @Builder.Default
    private boolean hasExternalReferral = false;

    @Field("has_external_spec_referral")
    @Builder.Default
    private boolean hasExternalSpecReferral = false;

    @Field("can_suggest_para_clinic")
    @Builder.Default
    private boolean canSuggestParaClinic = false;

    @Field("can_suggest_experimental")
    @Builder.Default
    private boolean canSuggestExperimental = false;

    private List<Question> questions;

}
