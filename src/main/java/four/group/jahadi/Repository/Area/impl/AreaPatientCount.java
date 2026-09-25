package four.group.jahadi.Repository.Area.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaPatientCount {
    private ObjectId _id;
    private Integer totalPatients;
}