package four.group.jahadi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import four.group.jahadi.Enums.AccessInModuleArea;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @JsonIgnore
    @Builder.Default
    private List<ObjectId> members = new ArrayList<>();

    @Transient
//    @JsonSerialize(using = OwnerSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<User> users;

    @Transient
//    @JsonSerialize(using = OwnerSerialization.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<User> secretaryUsers;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AccessInModuleArea> accesses;

    @JsonIgnore
    @Builder.Default
    private List<ObjectId> secretaries = new ArrayList<>();

}
