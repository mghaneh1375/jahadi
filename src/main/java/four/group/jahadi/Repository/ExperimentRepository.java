package four.group.jahadi.Repository;

import four.group.jahadi.Models.Experiment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@MyRepository(model = "Experiment")
public interface ExperimentRepository extends MongoRepository<Experiment, ObjectId>, FilterableRepository<Experiment> {

    @Query(value = "{visbility: true}", fields = "{createdAt: 0}")
    List<Experiment> justVisible();
}