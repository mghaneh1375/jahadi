package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.AccessInModuleArea;
import four.group.jahadi.Models.ListOfUsersSerialization;
import four.group.jahadi.Models.ObjectIdSerialization;
import four.group.jahadi.Models.User;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

import static four.group.jahadi.Utility.Utility.printNullableField;
import static four.group.jahadi.Utility.Utility.toStringOfList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuleInArea {

    @Id
    @MongoId
    @Field("_id")
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId id;

    @Field("module_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ObjectIdSerialization.class)
    private ObjectId moduleId;

    @Field("module_name")
    private String moduleName;

    @Field("module_tab_name")
    private String moduleTabName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ObjectId> members = new ArrayList<>();

    @Transient
    @JsonSerialize(using = ListOfUsersSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<User> users;

    @Transient
    @JsonSerialize(using = ListOfUsersSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<User> secretaryUsers;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AccessInModuleArea> accesses;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean hasDoctorAccess;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean hasSecretaryAccess;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ObjectId> secretaries = new ArrayList<>();

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public String toString() {
        return "{" +
                "\"id\":" + printNullableField(id) +
                ", \"moduleId\":" + printNullableField(moduleId) +
                ", \"moduleName\":" + printNullableField(moduleName) +
                ", \"moduleTabName\":" + printNullableField(moduleTabName) +
                ", \"members\":" + toStringOfList(members) +
                ", \"secretaries\":" + toStringOfList(secretaries) +
                '}';
    }
}
