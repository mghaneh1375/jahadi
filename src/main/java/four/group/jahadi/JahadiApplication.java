package four.group.jahadi;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import four.group.jahadi.DB.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan({"four.group.jahadi.*"})
@EntityScan("four.group.jahadi.*")
@Configuration
public class JahadiApplication {

    public static MongoDatabase mongoDatabase;
//    final static private ConnectionString connString = new ConnectionString(
//            "mongodb://localhost:27017/jahadi"
//    );

    public static UserRepository userRepository;

    private static void setupDB() {
        try {

//            MongoClientSettings settings = MongoClientSettings.builder()
//                    .applyConnectionString(connString)
//                    .retryWrites(true)
//                    .build();

//            MongoClient mongoClient = MongoClients.create(settings);
//            mongoDatabase = mongoClient.getDatabase("jahadi");

            userRepository = new UserRepository();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("Iran"));
//        setupDB();
//        new Thread(new Jobs()).start();
        SpringApplication.run(JahadiApplication.class, args);
    }

}
