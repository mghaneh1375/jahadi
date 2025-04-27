package four.group.jahadi.Utility;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

import static four.group.jahadi.JahadiApplication.ENCRYPTION_KEY;

public class SecurityUtil {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // 128-bit authentication tag

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encrypt(String plaintext) throws Exception {
        byte[] iv = new byte[12]; // 96-bit IV (recommended for GCM)
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv); // Generate a random IV

        SecretKeySpec keySpec = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String encryptedText) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedText);
        byte[] iv = new byte[12];
        System.arraycopy(combined, 0, iv, 0, iv.length);

        SecretKeySpec keySpec = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        byte[] encryptedBytes = new byte[combined.length - iv.length];
        System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    public static String hashPassword(String password) {
        SHA256Digest digest = new SHA256Digest();
        byte[] passwordBytes = password.getBytes();
        byte[] hash = new byte[digest.getDigestSize()];

        digest.update(passwordBytes, 0, passwordBytes.length);
        digest.doFinal(hash, 0);

        return Hex.toHexString(hash);
    }

    public static boolean verifyPassword(String inputPassword, String storedHash) {
        String inputHash = hashPassword(inputPassword);
        return inputHash.equals(storedHash);
    }

    public static void verifyCriticalFiles(String expectedCheckSum, String filePath) {
        String actual = null;
        try {
            actual = calculateChecksum(filePath);
            if (!decrypt(expectedCheckSum).equals(actual)) {
                throw new SecurityException("Critical file tampering detected!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String calculateChecksum(String filePath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes)
            hexString.append(String.format("%02x", b));

        return hexString.toString();
    }

    public static void main(String[] args) throws Exception {
        String originalValue = "d2f2069a1a68caac2bf675bb8e6ae0dfc150d7e3466f4a95ca0cf4c5c5bc94fb";
        String encryptedValue = encrypt(originalValue);
        System.out.println("Encrypted: " + encryptedValue);

        String decryptedValue = decrypt(encryptedValue);
        System.out.println("Decrypted: " + decryptedValue);

        System.out.println(hashPassword(encryptedValue));
    }
}