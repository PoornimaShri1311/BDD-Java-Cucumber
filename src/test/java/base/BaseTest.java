package base;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

@Getter
public class BaseTest {
    public static WebDriver driver;
    String currentDirectory = System.getProperty("user.dir");
    public Properties props = new Properties();
    public String configFolder = currentDirectory + "\\config.properties";
    private static final InvalidArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new InvalidArgumentException("Invalid browser.");

    @Before(order = 0)
    public void setup() {
        //  ExtentSparkReporter sparkReporter = ExtentManager.getSparkReporter();
        try {
            FileReader reader = new FileReader(configFolder);
            props.load(reader);
            initializeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initializeDriver() {
        if (props.getProperty("Browser").equals("chrome")) {
            driver = DriverFactory.setupChromeDriver();
        }
        if (props.get("Browser").equals("edge")) {
            driver = DriverFactory.setupEdgeDriver();
        }
        if (props.getProperty("Browser").equals("Default")) {
            DriverFactory.setupDefaultDriver();
        }
    }


    @Before(order = 1)
    public void scenarioSetUp(Scenario scenario) {
        //    Properties dataprops = new Properties();
        System.err.println(scenario);
    }


    @After(order = 1)
    public void takeScraenshotOnFailure(Scenario scenario) {

        if (scenario.isFailed()) {
            System.err.println("Taking Screenshot from After Method !");

            TakesScreenshot ts = (TakesScreenshot) driver;

            byte[] src = ts.getScreenshotAs(OutputType.BYTES);
            scenario.attach(src, "image/png", "screenshot");
        }
    }

    @After(order = 0)
    public void tearDown() {
        driver.close();
        driver.quit();
    }

    @AfterStep
    public void addScreenshot(Scenario scenario) throws IOException {
        FileReader reader = new FileReader(configFolder);
        props.load(reader);
        if (props.getProperty("ScreenshotOnFailure").equals("No")) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            byte[] fileContent = FileUtils.readFileToByteArray(screenshot);
            scenario.attach(fileContent, "image/png", "screenshot");
        } else if (props.getProperty("ScreenshotOnFailure").equals("Yes")) {
            if (scenario.isFailed()) {
                final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "image");
            }
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
