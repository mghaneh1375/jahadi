package four.group.jahadi.DTO.Trip;

import four.group.jahadi.Validator.ValidatedProject;
import four.group.jahadi.Validator.ValidatedTripStep2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ValidatedTripStep2
public class TripStep2Data extends TripStepData {

    private String name;

    private Long startAt;
    private Long endAt;

    private String dailyStartAt;
    private String dailyEndAt;

}
