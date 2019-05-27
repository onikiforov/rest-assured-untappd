package utils;

import config.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.System.out;

public class PropertiesUtils {
    public static String getProp(Properties properties, String key) {
        if ((key == null) || key.isEmpty()) {
            return "";
        } else {
            return properties.getProperty(key);
        }
    }

    public static Properties loadProperties(String configFileLocation) {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertiesUtils.class.getResourceAsStream(configFileLocation)) {
            properties.load(inputStream);
            properties.list(out);
        } catch (IOException e) {
            out.println(e);
        }
        return properties;
    }

    private static String getPropertiesPathByConfigType(String configType) {
        try {
            if (configType.equalsIgnoreCase("prod") || configType.equalsIgnoreCase("production")) {
                return Constants.PRODUCTION_CONFIG_PATH;
            }
        } catch (NullPointerException e) {
            out.println("Property configType is not specified!");
        }
        return Constants.DEVELOPMENT_CONFIG_PATH;
    }

    static String getTestDataPath() {
        return Constants.TEST_DATA_PATH;
    }

    private static String getEnvConfigFilename() {
        return Constants.ENV_CONFIG_FILENAME;
    }

    static String getTestDataFilename() {
        return Constants.TEST_DATA_FILENAME;
    }

    public static String getEnvironmentConfigPathByConfigType(String configType) {
        return getPropertiesPathByConfigType(configType) + getEnvConfigFilename();
    }
}
