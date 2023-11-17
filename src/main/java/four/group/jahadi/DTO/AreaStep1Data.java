package four.group.jahadi.DTO;

import four.group.jahadi.Validator.ValidatedArea;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ValidatedArea
public class AreaStep1Data {

    private ObjectId cityId;
    private Long lat;
    private Long lng;

}
