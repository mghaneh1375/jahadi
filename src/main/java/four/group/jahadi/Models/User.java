package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.*;
import lombok.*;
import org.bson.types.ObjectId;
import javax.persistence.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
@Builder
public class User extends Model {

    private String name;

    @JsonIgnore
    private String password;

    @Field("father_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fatherName;

    @Field("birth_day")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String birthDay;

    @Field("university_year")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String universityYear;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String university;

    @Field("NID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Sex sex;

    @Field("remove_at")
    @JsonIgnore
    private Long removeAt;

    @Field("group_id")
    @JsonIgnore
    private ObjectId groupId;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private AccountStatus groupStatus;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String abilities;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String diseases;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String allergies;

    @Field("blood_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BloodType bloodType;

    @Field("nearby_rel")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nearbyRel;

    @Field("nearby_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nearbyName;

    @Field("nearby_phone")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nearbyPhone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ColorSerialization.class)
    private Color color;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = PicSerialization.class)
    private String pic;

    @JsonIgnore
    private List<Access> accesses;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Access role;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Boolean hasActiveRegion;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Boolean hasActiveTask;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> notifs;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AccountStatus status;

    @Field("group_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String groupName;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer groupCode;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    @JsonSerialize(using = PicSerialization.class)
    private String groupPic;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String trips;

    @Field("end_manage_year")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String endManageYear;

    @Field("CID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer members;



    //group data

    @Field("establish_year")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String establishYear;

    @Field("atlas_code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String atlasCode;

    @Field("total_trips")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalTrips;

    @Field("recent_trips")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer recentTrips;

    @Field("total_members")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalMembers;

    @Field("recent_members")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer recentMembers;

    @Field("managers_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer managersCount;

    @Field("members_per_trip")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer membersPerTrip;

    @Field("trip_days")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer tripDays;

    @Field("regions_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer regionsCount;

    @Field("page_address")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pageAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String site;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String platform;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Lodgment lodgment;

    @Field("group_registration_place")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GroupRegistrationPlace groupRegistrationPlace;

    @Field("lodgment_other")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lodgmentOther;

    @Field("group_registration_place_other")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String groupRegistrationPlaceOther;

    @Field("trip_frequency")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TripFrequency tripFrequency;

    @Field("trip_radius")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TripRadius tripRadius;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tel;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DensityUnitCount densityUnitCount;

    @Field("pull_tooth_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer pullToothCount;

    @Field("restoration_tooth_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer restorationToothCount;

    @Field("rooter_canal_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer rooterCanalCount;

    @Field("imaging_equipment_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer imagingEquipmentCount;

    @Field("barometer_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer barometerCount;

    @Field("glucometer_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer glucometerCount;

    @Field("paravan_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer paravanCount;

    @Field("bed_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer bedCount;

    @Field("monitoring_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer monitoringCount;

    @Field("examination_set_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer examinationSetCount;

    @Field("ecg_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer ecgCount;

    @Field("electronic_shock_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer electronicShockCount;

    @Field("reviving_bag_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer revivingBagCount;

    @Field("pop_esmir_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer popEsmirCount;

    @Field("sono_kid_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer sonoKidCount;

    @Field("wife_examination_equipment_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer wifeExaminationEquipmentCount;

    @Field("sampling_equipment_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer samplingEquipmentCount;

    @Field("genetically_bed_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer geneticallyBedCount;

    @Field("optometry_equip_1")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer optometryEquip1;

    @Field("optometry_equip_2")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer optometryEquip2;

    @Field("optometry_equip_3")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer optometryEquip3;

    @Field("optometry_equip_4")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer optometryEquip4;

    @Field("optometry_equip_5")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer optometryEquip5;

    @Field("audiologist_equip_1")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer audiologistEquip1;

    @Field("audiologist_equip_2")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer audiologistEquip2;

    @Field("audiologist_equip_3")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer audiologistEquip3;

    @Field("audiologist_equip_4")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer audiologistEquip4;

    @Field("audiologist_equip_5")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer audiologistEquip5;

    @Field("imaging_equip_1")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer imagingEquip1;

    @Field("imaging_equip_2")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer imagingEquip2;

    @Field("imaging_equip_3")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer imagingEquip3;

    @Field("laboratory_equip_1")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer laboratoryEquip1;

    @Field("laboratory_equip_2")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer laboratoryEquip2;

    @Field("laboratory_equip_3")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer laboratoryEquip3;

    @Field("laboratory_equip_4")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer laboratoryEquip4;

    @Field("laboratory_equip_5")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer laboratoryEquip5;

    @Field("media_equip_1")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer mediaEquip1;

    @Field("media_equip_2")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer mediaEquip2;

    @Field("media_equip_3")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer mediaEquip3;

    @Field("custom_equips")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HashMap<String, Integer> customEquips;

    @Field("screening_sicknesses")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<String> screeningSicknesses;


    @Field("public_doctors")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer publicDoctors;

    @Field("donate_medicine")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer donateMedicine;

    @Field("medical_sections")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<MedicalSection> medicalSections;

    @Field("medical_expertises")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<MedicalExpertise> medicalExpertises;



    @Field("adult_education")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean adultEducation = false;

    @Field("child_education")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean childEducation = false;

    @Field("free_glass")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean freeGlass = false;

    @Field("free_hearing_aids")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean freeHearingAids = false;

    @Field("pop_esmir_test")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean popEsmirTest = false;

    @Field("cancer_test")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean cancerTest = false;

    @Field("social_work_assistance")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean socialWorkAssistance = false;

    @Field("quit_addiction")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean quitAddiction = false;

    @Field("family_psychology")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean familyPsychology = false;

    @Field("urine_analysis")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean urineAnalysis = false;

    @Field("blood_cells_count_test")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean bloodCellsCountTest = false;

    @Field("bio_chem_test")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean bioChemTest = false;

    @Field("hormon_test")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean hormonTest = false;

    @Field("movement_help_equipments")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean movementHelpEquipments = false;

    @JsonIgnore
    private Date deletedAt;
}
