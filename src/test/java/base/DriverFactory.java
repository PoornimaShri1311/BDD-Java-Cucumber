package base;

import Utilities.ConfigReader;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;


public class DriverFactory {
    private static final InvalidArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new InvalidArgumentException("Invalid browser.");
    static String currentDirectory = System.getProperty("user.dir");

    static ChromeDriver setupChromeDriver() {
        System.setProperty("webdriver.chrome.driver", currentDirectory + ConfigReader.getProperty("webdriver.chrome.driver"));
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
        ChromeOptions ops = new ChromeOptions();
//        ops.addArguments("--headless");
        ops.addArguments("--disable-notifications");
        ops.addArguments("--start-maximized");
        ops.addArguments("--use-fake-ui-for-media-stream");
        ops.addArguments("--disable-user-media-security=true");
        return new ChromeDriver(ops);
    }

    static EdgeDriver setupEdgeDriver() {
        System.setProperty("webdriver.edge.driver", currentDirectory + ConfigReader.getProperty("webdriver.edge.driver"));
        EdgeOptions E_OPTIONS = new EdgeOptions();
//        E_OPTIONS.addArguments("--headless");
        return new EdgeDriver(E_OPTIONS);
    }

    static void setupDefaultDriver() {
        throw ILLEGAL_ARGUMENT_EXCEPTION;
    }

}
