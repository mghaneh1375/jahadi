package four.group.jahadi.Validator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {

    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 50;

    // Regex patterns
    private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");

    public ValidationResult validate(String password) {
        ValidationResult result = new ValidationResult();

        if (password == null || password.trim().isEmpty()) {
            result.setValid(false);
            result.getMessages().add("لطفا رمزعبور را وارد نمایید");
            return result;
        }

        // Check minimum length
        if (password.length() < MIN_LENGTH) {
            result.setValid(false);
            result.getMessages().add("طول رمزعبور باید حداقل " + MIN_LENGTH + " کاراکتر باشد.");
        }

        // Check maximum length
        if (password.length() > MAX_LENGTH) {
            result.setValid(false);
            result.getMessages().add("طول رمزعبور نباید از " + MAX_LENGTH + " بیشتر باشد");
        }

        // Check uppercase letters
        if (!UPPER_CASE_PATTERN.matcher(password).find()) {
            result.setValid(false);
            result.getMessages().add("رمزعبور باید حداقل یک حرف بزرگ انگلیسی داشته باشد");
        }

        // Check lowercase letters
        if (!LOWER_CASE_PATTERN.matcher(password).find()) {
            result.setValid(false);
            result.getMessages().add("رمزعبور باید حداقل یک حرف کوچک انگلیسی داشته باشد");
        }

        // Check digits
        if (!DIGIT_PATTERN.matcher(password).find()) {
            result.setValid(false);
            result.getMessages().add("رمزعبور باید حداقل یک حرف عدد داشته باشد");
        }

        // If no errors found, password is valid
        if (result.getMessages().isEmpty()) {
            result.setValid(true);
            result.getMessages().add("Password is strong");
        }

        return result;
    }


    // Validation Result Class
    @Setter
    @Getter
    public static class ValidationResult {
        // Getters and setters
        private boolean valid;
        private List<String> messages = new ArrayList<>();

    }
}
