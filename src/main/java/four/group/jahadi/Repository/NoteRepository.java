package four.group.jahadi.Repository;

import four.group.jahadi.Models.Note;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, ObjectId>, FilterableRepository<Note> {

    @Query(value = "{user_id: ?0}", fields = "{'id': 1, 'title': 1, 'updatedAt': 1, 'description': 1}")
    List<Note> findByUserId(ObjectId userId);

    // use by reflection
    @Query(value = "{_id: {$in: ?0}}", delete = true)
    void deleteByIdsIn(List<ObjectId> ids);
}
