package p2p.utils;



import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AES256Util {

    private static final int IV_SIZE = 12;  // GCM standard IV size
    private static final int TAG_SIZE = 128;

    // Encrypts plaintext with AES-256-GCM
   public static String encrypt(String plaintext) throws Exception {
    byte[] key = AESKeyLoader.loadKey();
    byte[] iv = new byte[IV_SIZE];
    SecureRandom random = new SecureRandom();
    random.nextBytes(iv);

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_SIZE, iv);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

    byte[] encrypted = cipher.doFinal(plaintext.getBytes());

    byte[] combined = new byte[iv.length + encrypted.length];
    System.arraycopy(iv, 0, combined, 0, iv.length);
    System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);


    return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
}

    // Decrypts ciphertext with AES-256-GCM
    public static String decrypt(String base64CipherText) throws Exception {
          byte[] key = AESKeyLoader.loadKey();
        byte[] decoded = Base64.getDecoder().decode(base64CipherText);

        byte[] iv = new byte[IV_SIZE];
        byte[] encrypted = new byte[decoded.length - IV_SIZE];

        System.arraycopy(decoded, 0, iv, 0, IV_SIZE);
        System.arraycopy(decoded, IV_SIZE, encrypted, 0, encrypted.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_SIZE, iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }

    
}

