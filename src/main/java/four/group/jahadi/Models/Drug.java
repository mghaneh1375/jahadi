package four.group.jahadi.Models;

import four.group.jahadi.Enums.Drug.AmountOfUse;
import four.group.jahadi.Enums.Drug.HowToUse;
import four.group.jahadi.Enums.Drug.UseTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "drug")
public class Drug extends Model {

    private String name;
    private Integer price;
    @Field("how_to_uses")
    private List<HowToUse> howToUses;
    @Field("amount_of_uses")
    private List<AmountOfUse> amountOfUses;
    @Field("use_times")
    private List<UseTime> useTimes;
    private String description;
    private Integer available;
    private List<ObjectId> replacements;
    private Boolean visibility;
    private Integer priority;
}
