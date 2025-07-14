package p2p.utils;

import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

public class AESKeyLoader {

       public static byte[] loadKey() throws Exception {
        Properties props = new Properties();
        try (InputStream input = AESKeyLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }
            props.load(input);
        }

        String base64Key = props.getProperty("aes.key");
        if (base64Key == null) {
            throw new RuntimeException("aes.key not defined in config.properties");
        }

        return Base64.getDecoder().decode(base64Key);
    }


    
}
