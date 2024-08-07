package four.group.jahadi.Models;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

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
    private boolean isReferral = true;

    @Field("can_suggest_drug")
    @Builder.Default
    private boolean canSuggestDrug = false;

    @Field("can_suggest_experiment")
    @Builder.Default
    private boolean canSuggestExperiment = false;
}
