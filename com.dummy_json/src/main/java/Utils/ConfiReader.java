package Utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfiReader {

    private static Properties prop;

    static {
        try {
            InputStream fis = ConfiReader.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");

            prop = new Properties();
            prop.load(fis);

        } catch (Exception e) {
            throw new RuntimeException("Config load failed");
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }
}