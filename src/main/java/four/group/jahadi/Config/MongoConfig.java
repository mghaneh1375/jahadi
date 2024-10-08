package four.group.jahadi.Config;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory mongoDatabaseFactory){
        MongoTransactionManager manager = new MongoTransactionManager(mongoDatabaseFactory, TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build());
        manager.setRollbackOnCommitFailure(true);
        return manager;
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    public MongoClient getMongoClient() {
        return mongoClient();
    }
}