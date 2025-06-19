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

import static four.group.jahadi.Utility.Utility.printNullableDate;
import static four.group.jahadi.Utility.Utility.printNullableField;

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
    private HowToUse howToUse;
    @Field("amount_of_use")
    private AmountOfUse amountOfUse;
    @Field("use_time")
    private UseTime useTime;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String howToUseFa;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String amountOfUseFa;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String useTimeFa;

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(this.getId()) +
                ", \"createdAt\":" + printNullableDate(this.getCreatedAt()) +
                ", \"drugName\":" + printNullableField(drugName) +
                ", \"drugId\":" + printNullableField(drugId) +
                ", \"userId\":" + printNullableField(userId) +
                ", \"howToUse\":" + printNullableField(howToUse) +
                ", \"amountOfUse\":" + printNullableField(amountOfUse) +
                ", \"useTime\":" + printNullableField(useTime) +
                "}\n";
    }
}
