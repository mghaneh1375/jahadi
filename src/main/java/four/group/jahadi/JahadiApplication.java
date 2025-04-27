package four.group.jahadi;

import four.group.jahadi.Utility.SecurityUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import javax.annotation.PostConstruct;

@SpringBootApplication()
@OpenAPIDefinition(info = @Info(title = "Jahadi API", version = "2.0", description = "Jahadi Information"))
@EnableMongoAuditing
public class JahadiApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Value("${app.password.hash}")
    private String encryptedPasswordHash;

    @Value("${app.checksum}")
    private String encryptedChecksum;

    @Value("${app.license.key}")
    private String licenseKey;

    @Value("${app.security.level}")
    private String securityLevel;

    @Value("${app.security.dev}")
    private String securityDevLevel;

    @Value("${app.main-file-path}")
    private String mainFilePath;

    @Autowired
    private ApplicationContext context;
//    public static String ENCRYPTION_KEY;

    public static String ENCRYPTION_KEY = "|)q,xeI3w4g@WtH[`7>}f6vN$Q3iY)[P";

    public static void main(String[] args) {
//        if (!"SECURE_WRAPPER".equals(System.getenv("APP_SAFE_START"))) {
//            System.err.println("Must be started via launch.sh");
//            System.exit(1);
//        }
//        TimeZone.setDefault(TimeZone.getTimeZone("Iran"));
//        new Thread(new Jobs()).start();
//        ENCRYPTION_KEY = System.getProperty("encryptionPassword");
//        System.out.println("encryptionPassword " + ENCRYPTION_KEY + " " + ENCRYPTION_KEY.length());
//        String inputLicenseKey = System.getProperty("licenseKey");
//        System.out.println("inputLicenseKey " + inputLicenseKey + " " + inputLicenseKey.length());
//        String inputPassword = System.getProperty("password");
//        System.out.println("inputPassword " + inputPassword + " " + inputPassword.length());
        SpringApplication.run(JahadiApplication.class, args);
//        try {
//            String s = calculateChecksum("C:\\Users\\user\\IdeaProjects\\Jahadi\\target\\Jahadi-0.0.1-SNAPSHOT-obfuscated.jar");
//            System.out.println(s);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
    @PostConstruct
    public void run() {
//        SecurityUtil.verifyCriticalFiles(encryptedChecksum, mainFilePath);
        try {
            if(SecurityUtil.verifyPassword(securityDevLevel, securityLevel))
                return;

            String inputLicenseKey = System.getProperty("licenseKey");

            System.out.println("licenseKey " + licenseKey);
            System.out.println("encryptedPasswordHash " + encryptedPasswordHash);

            String decryptedLicenseKey = SecurityUtil.decrypt(licenseKey);
            System.out.println("decryptedLicenseKey " + decryptedLicenseKey + " " + decryptedLicenseKey.length());
            if (!SecurityUtil.verifyPassword(inputLicenseKey, decryptedLicenseKey)) {
                System.out.println("Invalid license key. Exiting...");
                shutdown();
            }

            // Decrypt the password hash
            String decryptedPasswordHash = SecurityUtil.decrypt(encryptedPasswordHash);
            // Verify password
            String inputPassword = System.getProperty("password");

            if (!SecurityUtil.verifyPassword(inputPassword, decryptedPasswordHash)) {
                System.out.println("Incorrect password. Exiting...");
                shutdown();
            }
            System.out.println("Password verified.");

            // Decrypt the checksum
//            String decryptedChecksum = SecurityUtil.decrypt(encryptedChecksum);
//            // Verify checksum
//            String jarPath = ObjectMetaData.Application.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//            System.out.println(jarPath);
//            String actualChecksum = calculateChecksum(jarPath);
//            if (!decryptedChecksum.equals(actualChecksum)) {
//                System.out.println("Checksum verification failed. The JAR file may have been tampered with.");
//                shutdown();
//            }
//            System.out.println("Checksum verified. Starting application...");
        } catch (Exception e) {
            System.out.println("Error during decryption or verification: " + e.getMessage());
            e.printStackTrace();
            shutdown();
        }
    }

    private void shutdown() {
        // Gracefully shut down the Spring application
        int exitCode = SpringApplication.exit(context, () -> 1); // Return a non-zero exit code
        System.exit(exitCode);
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
