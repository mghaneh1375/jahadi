package four.group.jahadi.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "drug")
public class Drug {

    @Id
    @MongoId
    @Field("_id")
    private ObjectId _id;

    private String name;
    private int price;
    private String howToUse;
    private String description;
    private int available;
    private List<ObjectId> replacements;
}
