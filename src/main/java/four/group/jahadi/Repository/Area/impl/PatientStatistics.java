package four.group.jahadi.Repository.Area.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientStatistics {
    private CombinedId _id;
    private Integer total;
    private Integer accepted;
    private Integer acceptedByDoctor;
}