package four.group.jahadi.Service;

import four.group.jahadi.Models.Model;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class IOService {

    public void export(
            List<? extends Model> models,
            ServletOutputStream outputStream,
            String modelName
    ) {
        try {
            outputStream.write(String.format("\n\n*******%s*******\n\n", modelName).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        models.forEach(model -> {
            try {
                outputStream.write(model.toString().getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
