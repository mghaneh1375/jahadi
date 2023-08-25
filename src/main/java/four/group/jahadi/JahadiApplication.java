package four.group.jahadi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.TimeZone;

@SpringBootApplication()
@EnableMongoAuditing
public class JahadiApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("Iran"));
//        setupDB();
//        new Thread(new Jobs()).start();
        SpringApplication.run(JahadiApplication.class, args);
    }

}
