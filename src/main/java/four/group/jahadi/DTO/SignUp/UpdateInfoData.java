package four.group.jahadi.DTO.SignUp;

import four.group.jahadi.Enums.*;
import four.group.jahadi.Validator.JustNumeric;
import four.group.jahadi.Validator.Positive;
import four.group.jahadi.Validator.SignUp.ValidatedUpdateInfo;
import four.group.jahadi.Validator.Year;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidatedUpdateInfo
public class UpdateInfoData {
    private String abilities;
    private String diseases;
    private String allergies;
    private BloodType bloodType;

    @Size(min = 3, max = 50)
    private String nearbyName;
    
    @Size(min = 2, max = 50)
    private String nearbyRel;
    
    @JustNumeric
    private String nearbyPhone;
    
    @Size(min = 3, max = 50)
    private String name;

    @Size(min = 3, max = 50)
    private String fatherName;

    private String birthDay;
    
    @Size(min = 3, max = 50)
    private String field;

    @Size(min = 3, max = 50)
    private String university;

    @Year
    private String universityYear;

    @Year
    private String endManageYear;

    @JustNumeric
    private String nid;
    private Sex sex;

    // *************** GROUP SECTION *****************
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

    
    @Size(min = 2, max = 100)
    private String lodgmentOther;

    
    @Size(min = 2, max = 100)
    private String groupRegistrationPlaceOther;

    
    @Size(min = 3, max = 200)
    private String address;

    
    @JustNumeric
    @Size(min = 5, max = 11)
    private String tel;

    private DensityUnitCount densityUnitCount;

    
    @Positive
    private Integer pullToothCount;

    
    @Positive
    private Integer restorationToothCount;

    
    @Positive
    private Integer rooterCanalCount;

    
    @Positive
    private Integer imagingEquipmentCount;

    // medical

    
    @Positive
    private Integer barometerCount;

    
    @Positive
    private Integer glucometerCount;

    
    @Positive
    private Integer paravanCount;

    
    @Positive
    private Integer bedCount;

    
    @Positive
    private Integer monitoringCount;

    
    @Positive
    private Integer examinationSetCount;

    
    @Positive
    private Integer ecgCount;

    
    @Positive
    private Integer electronicShockCount;

    
    @Positive
    private Integer revivingBagCount;

    // Midwifery

    
    @Positive
    private Integer popEsmirCount;

    
    @Positive
    private Integer sonoKidCount;

    
    @Positive
    private Integer wifeExaminationEquipmentCount;

    
    @Positive
    private Integer samplingEquipmentCount;

    
    @Positive
    private Integer geneticallyBedCount;

    // Optometry

    
    @Positive
    private Integer optometryEquip1;

    
    @Positive
    private Integer optometryEquip2;

    
    @Positive
    private Integer optometryEquip3;

    
    @Positive
    private Integer optometryEquip4;

    
    @Positive
    private Integer optometryEquip5;

    // Audiologists

    
    @Positive
    private Integer audiologistEquip1;

    
    @Positive
    private Integer audiologistEquip2;

    
    @Positive
    private Integer audiologistEquip3;

    
    @Positive
    private Integer audiologistEquip4;

    
    @Positive
    private Integer audiologistEquip5;

    // Imaging

    
    @Positive
    private Integer imagingEquip1;

    
    @Positive
    private Integer imagingEquip2;

    
    @Positive
    private Integer imagingEquip3;

    // Laboratory

    
    @Positive
    private Integer laboratoryEquip1;

    
    @Positive
    private Integer laboratoryEquip2;

    
    @Positive
    private Integer laboratoryEquip3;

    
    @Positive
    private Integer laboratoryEquip4;

    
    @Positive
    private Integer laboratoryEquip5;

    // Media

    
    @Positive
    private Integer mediaEquip1;

    
    @Positive
    private Integer mediaEquip2;

    
    @Positive
    private Integer mediaEquip3;

    private HashMap<String, Integer> customEquips;


    List<String> screeningSicknesses;

    
    @Positive
    private Integer publicDoctors;

    
    @Positive
    private Integer donateMedicine;

    List<MedicalSection> medicalSections;
    List<MedicalExpertise> medicalExpertises;

    private Boolean adultEducation;
    private Boolean childEducation;
    private Boolean freeGlass;
    private Boolean freeHearingAids;
    private Boolean popEsmirTest;
    private Boolean cancerTest;
    private Boolean socialWorkAssistance;
    private Boolean quitAddiction;
    private Boolean familyPsychology;
    private Boolean urineAnalysis;
    private Boolean bloodCellsCountTest;
    private Boolean bioChemTest;
    private Boolean hormonTest;
    private Boolean movementHelpEquipments;

}
