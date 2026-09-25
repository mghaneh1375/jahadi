package four.group.jahadi.DTO.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Models.JustDateSerialization;
import four.group.jahadi.Models.ObjectIdSerialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaStat {
    private String role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String city;
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = JustDateSerialization.class)
    private LocalDateTime start;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = JustDateSerialization.class)
    private LocalDateTime end;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String tripName;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId tripId;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String ownerName;

    private Boolean isOwner;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer totalPatients;

    @JsonIgnore
    private List<ObjectId> members;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer maleMembers;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Integer femaleMembers;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Boolean finalized;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Boolean stopReception;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Boolean hasTrainSection;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Boolean hasInsuranceSection;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Boolean hasLaboratorySection;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private Boolean hasPharmacySection;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private List<ModuleStat> moduleStats;
}
