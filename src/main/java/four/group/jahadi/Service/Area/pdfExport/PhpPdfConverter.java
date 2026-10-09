package four.group.jahadi.Service.Area.pdfExport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.UUID;

@Component
public class PhpPdfConverter {

    @Value("${app.pdf.service.dir:/absolute/path/to/pdf-service}")
    private String serviceDir;

    @Value("${app.pdf.service.php-bin:php}")
    private String phpBin;

    @Value("${app.pdf.service.timeout-seconds:30}")
    private int timeoutSeconds;

    public byte[] convertHtmlToPdf(String html) {
        Path inputFile  = null;
        Path outputFile = null;

        try {
            Path tmpDir = Paths.get(serviceDir, "tmp");
            Files.createDirectories(tmpDir);

            String uniqueId = UUID.randomUUID().toString();
            inputFile  = tmpDir.resolve("in-"  + uniqueId + ".html");
            outputFile = tmpDir.resolve("out-" + uniqueId + ".pdf");

            Files.write(inputFile, html.getBytes(StandardCharsets.UTF_8));

            ProcessBuilder pb = new ProcessBuilder(
                    phpBin,
                    Paths.get(serviceDir, "generate_pdf.php").toString(),
                    inputFile.toString(),
                    outputFile.toString()
            );
            pb.directory(Paths.get(serviceDir).toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();

            String processOutput = new String(
                    process.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            boolean finished = process.waitFor(timeoutSeconds, java.util.concurrent.TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                throw new RuntimeException("PHP conversion timed out");
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new RuntimeException(
                    "PHP failed (exit " + exitCode + "): " + processOutput
                );
            }

            if (!Files.exists(outputFile)) {
                throw new RuntimeException("PDF not created by PHP");
            }

            return Files.readAllBytes(outputFile);

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("PDF conversion failed", e);

        } finally {
            deleteQuietly(inputFile);
            deleteQuietly(outputFile);
        }
    }

    private void deleteQuietly(Path path) {
        if (path == null) return;
        try { Files.deleteIfExists(path); } catch (IOException ignored) {}
    }
}