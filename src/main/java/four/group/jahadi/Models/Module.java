package four.group.jahadi.Models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static four.group.jahadi.Utility.Utility.printNullableDate;
import static four.group.jahadi.Utility.Utility.printNullableField;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "module")
@Builder
public class Module extends Model {

    private String name;

    @Field("tab_name")
    private String tabName;

    @Field("sub_modules")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SubModule> subModules;

    private String icon;

    @Field("is_referral")
    @Builder.Default
    @JsonProperty("isReferral")
    private boolean isReferral = true;

    @Field("can_suggest_drug")
    @Builder.Default
    @JsonProperty("canSuggestDrug")
    private boolean canSuggestDrug = false;

    @Field("can_suggest_experiment")
    @Builder.Default
    @JsonProperty("canSuggestExperiment")
    private boolean canSuggestExperiment = false;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                ", \"name\":" + printNullableField(name) +
                ", \"tabName\":" + printNullableField(tabName) +
                ", \"subModules\":" + subModules +
                ", \"icon\":" + printNullableField(icon) +
                ", \"isReferral\":" + isReferral +
                ", \"canSuggestDrug\":" + canSuggestDrug +
                ", \"canSuggestExperiment\":" + canSuggestExperiment +
                "}\n";
    }
}
