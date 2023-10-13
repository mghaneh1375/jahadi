package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import four.group.jahadi.Enums.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
@Builder
public class User extends Model implements Serializable {

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
    private String nid;

    private String phone;
    private Sex sex;


    @Field("remove_at")
    @JsonIgnore
    private Long removeAt;

    @Field("group_id")
    @JsonIgnore
    private ObjectId groupId;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String abilities;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String diseases;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String allergies;

    @Field("blood_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BloodType bloodType;

    @Field("nearby_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nearbyName;

    @Field("nearby_phone")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nearbyPhone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Color color;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pic;

    @JsonIgnore
    private List<Access> accesses;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> notifs;

    private AccountStatus status;

    @Field("group_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String groupName;

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


}
