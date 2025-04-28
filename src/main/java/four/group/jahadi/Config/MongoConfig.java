package four.group.jahadi.Config;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static four.group.jahadi.Utility.Utility.tehranZoneId;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
        MongoTransactionManager manager = new MongoTransactionManager(mongoDatabaseFactory, TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build());
        manager.setRollbackOnCommitFailure(true);
        return manager;
    }

    @WritingConverter
    public class LocalDateTimeWriter implements Converter<LocalDateTime, Date> {
        @Override
        public Date convert(LocalDateTime source) {
            ZonedDateTime tehranTime = source.atZone(tehranZoneId);
            ZonedDateTime utcTime = tehranTime.withZoneSameLocal(ZoneOffset.UTC);
            return Date.from(utcTime.toInstant());
        }
    }

    @ReadingConverter
    public class LocalDateTimeReader implements Converter<Date, LocalDateTime> {
        @Override
        public LocalDateTime convert(Date source) {
            Instant instant = source.toInstant();
            ZonedDateTime tehranTime = instant.atZone(tehranZoneId);
            ZonedDateTime utcTime = tehranTime.withZoneSameInstant(ZoneOffset.UTC);
            return utcTime.toLocalDateTime();
        }
    }

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                List.of(
                        new LocalDateTimeWriter(),
                        new LocalDateTimeReader()
                )
        );
    }


    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
}