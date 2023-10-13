package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.*;
import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.Positive;
import four.group.jahadi.Validator.SignUp.ValidatedGroupSignUpForm;
import four.group.jahadi.Validator.Year;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedGroupSignUpForm
public class GroupSignUpData {

    // user data

    @Size(min = 3, max = 50)
    private String name;

    @Size(min = 3, max = 50)
    private String fatherName;

    private String birthDay;

    @Year
    private String universityYear;

    @Year
    private String endManageYear;

    @Size(min = 3, max = 50)
    private String field;

    @Size(min = 3, max = 50)
    private String university;

    @JustNumeric
    private String nid;

    @JustNumeric
    private String cid;

    @JustNumeric
    private String phone;

    private Sex sex;

    private String password;
    private String passwordRepeat;


//    // group data
//    @Min(3)
//    @Max(50)
//    private String groupName;
//
//    @Year
//    private String establishYear;
//
//    @JustNumeric
//    private String atlasCode;
//
//    @Positive
//    private Integer totalTrips;
//
//    @Positive
//    private Integer recentTrips;
//
//    @Positive
//    private Integer totalMembers;
//
//    @Positive
//    private Integer recentMembers;
//
//    @Positive
//    private Integer managersCount;
//
//    @Positive
//    private Integer membersPerTrip;
//
//    @Positive
//    private Integer tripDays;
//
//    @Positive
//    private Integer regionsCount;
//
//    @Min(2)
//    @Max(50)
//    private String pageAddress;
//
//    @Min(7)
//    @Max(200)
//    private String site;
//
//    private String platform;
//    private Lodgment lodgment;
//    private GroupRegistrationPlace groupRegistrationPlace;
//    private TripFrequency tripFrequency;
//    private TripRadius tripRadius;
//
//    @Min(3)
//    @Max(200)
//    private String address;
//
//    @JustNumeric
//    private String tel;
//
//    // facilities
//
//    // density
//    private DensityUnitCount densityUnitCount;
//
//    @Positive
//    private Integer pullToothCount;
//
//    @Positive
//    private Integer restorationToothCount;
//
//    @Positive
//    private Integer rooterCanalCount;
//
//    @Positive
//    private Integer imagingEquipmentCount;
//
//    // medical
//
//    @Positive
//    private Integer barometerCount;
//
//    @Positive
//    private Integer glucometerCount;
//
//    @Positive
//    private Integer paravanCount;
//
//    @Positive
//    private Integer bedCount;
//
//    @Positive
//    private Integer monitoringCount;
//
//    @Positive
//    private Integer examinationSetCount;
//
//    @Positive
//    private Integer ecgCount;
//
//    @Positive
//    private Integer electronicShockCount;
//
//    @Positive
//    private Integer revivingBagCount;
//
//    // Midwifery
//
//    @Positive
//    private Integer popEsmirCount;
//
//    @Positive
//    private Integer sonoKidCount;
//
//    @Positive
//    private Integer wifeExaminationEquipmentCount;
//
//    @Positive
//    private Integer samplingEquipmentCount;
//
//    @Positive
//    private Integer geneticallyBedCount;
//
//    // Optometry
//
//
//
//    List<String> screeningSicknesses;
//
//    @Positive
//    private Integer publicDoctors;
//
//    @Positive
//    private Integer donateMedicine;
//
//    List<MedicalSection> medicalSections;
//    List<MedicalExpertise> medicalExpertises;

}
