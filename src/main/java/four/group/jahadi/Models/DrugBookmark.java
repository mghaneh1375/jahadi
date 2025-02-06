package four.group.jahadi.Models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Drug.AmountOfUse;
import four.group.jahadi.Enums.Drug.HowToUse;
import four.group.jahadi.Enums.Drug.UseTime;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "drug_bookmark")
@Builder
public class DrugBookmark extends Model {
    @Field("drug_name")
    private String drugName;
    @Field("drug_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId drugId;
    @Field("user_id")
    private ObjectId userId;
    @Field("how_to_use")
    private HowToUse howToUses;
    @Field("amount_of_use")
    private AmountOfUse amountOfUses;
    @Field("use_time")
    private UseTime useTimes;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String howToUsesFa;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String amountOfUsesFa;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String useTimesFa;
}
