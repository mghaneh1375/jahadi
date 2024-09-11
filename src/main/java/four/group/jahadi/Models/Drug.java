package four.group.jahadi.Models;

import four.group.jahadi.Enums.Drug.*;
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
    @Field("drug_type")
    private DrugType drugType;
    private String name;
    private String producer;
    private Integer price;
    private String description;
    private Integer available;
    private List<ObjectId> replacements;
    private Boolean visibility;
    private Integer priority;
    private DrugLocation location;
    @Field("box_no")
    private String boxNo;
}
