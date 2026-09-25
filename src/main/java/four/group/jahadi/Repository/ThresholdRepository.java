package four.group.jahadi.Repository;

import four.group.jahadi.Models.ReportThreshold;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThresholdRepository extends MongoRepository<ReportThreshold, ObjectId> {
    @Query(value = "{reportType: ?0}")
    Optional<ReportThreshold> findByReportType(String reportType);
}
