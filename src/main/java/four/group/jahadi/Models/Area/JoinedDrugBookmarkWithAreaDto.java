package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.Drug.AmountOfUse;
import four.group.jahadi.Enums.Drug.HowToUse;
import four.group.jahadi.Enums.Drug.UseTime;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinedDrugBookmarkWithAreaDto {
    @Field("_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId areaDrugId;
    @Field("drug_name")
    private String drugName;
    @Field("drug_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId drugId;
    @Field("how_to_use")
    private HowToUse howToUse;
    @Field("amount_of_use")
    private AmountOfUse amountOfUse;
    @Field("use_time")
    private UseTime useTime;
    @Field("amount")
    private Integer amount;

    private String howToUseFa;
    private String amountOfUseFa;
    private String useTimeFa;
}
