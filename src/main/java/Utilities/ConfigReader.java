package Utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    static String currentDirectory = System.getProperty("user.dir");
    private static final String CONFIG_FILE = currentDirectory + "\\config.properties";
    private static Properties properties;

    /*public ConfigReader() {
        this.properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Sorry, unable to find " + CONFIG_FILE);
                return;
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    static {
        try {
            FileReader fr = new FileReader(CONFIG_FILE);
            properties = new Properties();
            if (fr == null) {
                System.err.println("Sorry, unable to find " + CONFIG_FILE);
            } else {
                properties.load(fr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Sorry, unable to find " + CONFIG_FILE);
            } else {
                properties.load(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}