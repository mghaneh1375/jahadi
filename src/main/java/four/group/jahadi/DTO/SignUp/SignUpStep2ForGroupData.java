package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.GroupRegistrationPlace;
import four.group.jahadi.Enums.Lodgment;
import four.group.jahadi.Enums.TripFrequency;
import four.group.jahadi.Enums.TripRadius;
import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.Positive;
import four.group.jahadi.Validator.SignUp.ValidatedSignUpFormStep2ForGroups;
import four.group.jahadi.Validator.Year;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedSignUpFormStep2ForGroups
public class SignUpStep2ForGroupData {

    @Size(min = 3, max = 50)
    private String groupName;

    @Year
    private String establishYear;

    @JustNumeric
    private String atlasCode;

    @Positive
    private Integer totalTrips;

    @Positive
    private Integer recentTrips;

    @Positive
    private Integer totalMembers;

    @Positive
    private Integer recentMembers;

    @Positive
    private Integer managersCount;

    @Positive
    private Integer membersPerTrip;

    @Positive
    private Integer tripDays;

    @Positive
    private Integer regionsCount;

    @Size(min = 2, max = 50)
    private String pageAddress;

    @Size(min = 7, max = 50)
    private String site;

    private String platform;
    private Lodgment lodgment;
    private GroupRegistrationPlace groupRegistrationPlace;
    private TripFrequency tripFrequency;
    private TripRadius tripRadius;

    @Size(min = 3, max = 200)
    private String address;

    @JustNumeric
    private String tel;

}
