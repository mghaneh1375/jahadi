package four.group.jahadi.Repository.Area.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupStatisticsResult {
    private List<FieldOfStudyCount> byFieldOfStudy;
    private List<BirthDecadeCount> byBirthDecade;
    private List<TripBucketCount> byTripBucket;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class FieldOfStudyCount {
    private String fieldOfStudy;
    private Long count;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class BirthDecadeCount {
    private Integer decade;
    private Long count;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class TripBucketCount {
    private String label;
    private Long count;
}