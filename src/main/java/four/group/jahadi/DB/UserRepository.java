package four.group.jahadi.DB;

import com.mongodb.client.FindIterable;
import four.group.jahadi.JahadiApplication;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

public class UserRepository extends Common {

    public final static String FOLDER = "usersPic";

    public UserRepository() {
        init();
    }

    public synchronized Document findByUsername(String username) {

        Document user = isInCache(table, username);
        if (user != null)
            return user;

        FindIterable<Document> cursor = documentMongoCollection.find(
                or(
                        eq("phone", username),
                        eq("mail", username)

                )
        );
        if (cursor.iterator().hasNext()) {
            Document doc = cursor.iterator().next();
            return findById(doc.getObjectId("_id"));
        }

        return null;
    }

    @Override
    void init() {
        table = "user";
//        documentMongoCollection = JahadiApplication.mongoDatabase.getCollection(table);
    }
}
