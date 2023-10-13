package four.group.jahadi.DTO.Trip;

import four.group.jahadi.Validator.ValidatedTripStep1;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ValidatedTripStep1
public class TripStep1Data extends TripStepData {

    private Boolean writeAccess;
    private ObjectId owner;

}
