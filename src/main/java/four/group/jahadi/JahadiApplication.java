package four.group.jahadi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.TimeZone;

@SpringBootApplication()
@OpenAPIDefinition(info = @Info(title = "Jahadi API", version = "2.0", description = "Jahadi Information"))
@EnableMongoAuditing
public class JahadiApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    public static void main(String[] args) {
//        TimeZone.setDefault(TimeZone.getTimeZone("Iran"));
//        new Thread(new Jobs()).start();
        SpringApplication.run(JahadiApplication.class, args);
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    static {
        SpringDocUtils.getConfig().replaceWithSchema(ObjectId.class, new StringSchema());
    }

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()));
    }
}
