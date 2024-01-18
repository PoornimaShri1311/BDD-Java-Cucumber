package stepDefinitions;

import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import customFiles.SFCustomScripts;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SF {
    private static Scenario currentScenario;
    private static final Logger logger = Logger.getLogger(SF.class.getName());
    String currentDirectory = System.getProperty("user.dir");
    //    String fileDoc = currentDirectory + "\\Screenshots\\";
    BaseTest baseTest = new BaseTest();
    private static final Map<String, WebElement> elementMap = new HashMap<>();
//    private final pageReader pr = new pageReader();

    @Before(order = 0)
    public void setUp() {
        baseTest.setup();
    }


    @After(order = 0)
    public void tearDown() {
        baseTest.tearDown();
    }

    @AfterStep(order = 1)
    public void addScreenshot(Scenario scenario) throws IOException {
        baseTest.addScreenshot(scenario);
    }

    public static Scenario getCurrentScenario() {
        return currentScenario;
    }

    @Given("I am on homepage url \"([^\\\"]*)\"$")
    public void i_am_on_home_page_url(String arg1) {
        SFCustomScripts.launchURL(arg1);
    }

    @Then("I click \"([^\\\"]*)\" button of \"([^\\\"]*)\"$")
    public void iClickButtonOf(String elementName, String pageName) throws IOException {
        SFCustomScripts.clickButton(elementName, pageName);
    }

    @Then("User waits for page to get loaded")
    public void user_waits_for_page_to_get_loaded() {
        Duration pageLoadTimeout = Duration.ofSeconds(30);
        WebDriverWait wait = new WebDriverWait(baseTest.driver, pageLoadTimeout);
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

    }

    @Then("User waits for sleep time")
    public void userWaitsForSleepTime() throws InterruptedException {
        Thread.sleep(5000);
    }


    @When("I enter \"([^\\\"]*)\" in \"([^\\\"]*)\" field of \"([^\\\"]*)\".\"([^\\\"]*)\"$")
    public void iEnterUsernameInUsernameFieldOfLoginpage(String testData, String elementName, String pageName, String continueOnFailure) {
        SFCustomScripts.enterDataInTestField(testData, elementName, pageName, continueOnFailure);
    }

    @And("User waits for \"([^\\\"]*)\" element to be \"([^\\\"]*)\" in \"([^\\\"]*)\".\"([^\\\"]*)\"$")
    public void userWaitsForElementToBeIn(String arg0, String arg1, String arg2, String continueOnFailure) {
        SFCustomScripts.waitForElementInPage(arg0, arg1, arg2, continueOnFailure);
    }

    @And("I select \"([^\\\"]*)\" from \"([^\\\"]*)\" of \"([^\\\"]*)\"$")
    public void iSelectFromOf(String arg0, String arg1, String arg2) throws IOException {
        SFCustomScripts.selectDataFromDD(arg0, arg1, arg2);
    }

    @When("User generates random \"([^\\\"]*)\" value and enters in \"([^\\\"]*)\" field of \"([^\\\"]*)\"$")
    public void userGeneratesRandomValueAndStoresInFieldOf(String arg0, String arg1, String arg2) {
        SFCustomScripts.generateRandomValueAndStore(arg0, arg1, arg2);
    }

    @And("User reads \"([^\\\"]*)\" and stores \"([^\\\"]*)\" of \"([^\\\"]*)\"$")
    public void userReadsAndStoresOf(String arg0, String arg1, String arg2) throws IOException {
        SFCustomScripts.readAndStore(arg0, arg1, arg2);
    }

    @Then("I click \"([^\\\"]*)\" button of \"([^\\\"]*)\".\"([^\\\"]*)\"$")
    public void iClickButtonOf(String elementName, String pageName, String continueOnFailure) {
        SFCustomScripts.buttonClick(elementName, pageName, continueOnFailure);
    }

    @Then("User verify if \"([^\\\"]*)\" is \"([^\\\"]*)\" in \"([^\\\"]*)\".\"([^\\\"]*)\"$")
    public void userVerifyIfIsIn(String elementName, String visibility, String pageName, String continueOnFailure) {
        SFCustomScripts.verifyIfElementDisplayed(elementName, visibility, pageName, continueOnFailure);

    }

    @When("User enters \"([^\\\"]*)\" in \"([^\\\"]*)\" field of \"([^\\\"]*)\".\"([^\\\"]*)\"$")
    public void userEntersFirstNameInFieldOf(String fieldValue, String elementName, String pageName, String continueOnFailure) {
        SFCustomScripts.enterDataForOutline(fieldValue, elementName, pageName, continueOnFailure);
    }

    /*private WebElement readExcelToFindElementwithMap(String elementNameToFind, String pageName) {
//        String filePath = "C:\\Users\\Poorn\\OneDrive\\Desktop\\AVIV\\Aviv-QA-Web-Technical-Test\\src\\test\\java\\pages\\Pages.xlsx";
        String currentDirectory = System.getProperty("user.dir");
        String filePath = currentDirectory + "\\src\\test\\java\\pages\\Pages.xlsx";
        String sheetName = pageName;

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                // Start reading data from the second row (index 1)
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    if (row != null) {
                        String currentElementName = getStringCellValue(row.getCell(0));
                        String locatorType = getStringCellValue(row.getCell(1));
                        String locatorValue = getStringCellValue(row.getCell(2));

                        if (currentElementName.equals(elementNameToFind)) {
                            WebElement element = findElement(currentElementName, locatorType, locatorValue);
                            // Add the element to the map
                            elementMap.put(elementNameToFind, element);
                            return element;
                        }
                    }
                }
            } else {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file", e);
        }
        return null;
    }
*/
   /* private String readTestData(String elementNameToFind, String pageName) {
        Map<String, String> testDataMap = new HashMap<>();
        // Specify the path to your Excel file
//        String filePath = "C:\\Users\\Poorn\\OneDrive\\Desktop\\AVIV\\Aviv-QA-Web-Technical-Test\\src\\test\\java\\pages\\Pages.xlsx";
        String currentDirectory = System.getProperty("user.dir");
        String filePath = currentDirectory + "\\src\\test\\java\\pages\\Pages.xlsx";
        String sheetName = pageName;

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                // Start reading data from the second row (index 1)
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        String currentElementName = getStringCellValue(row.getCell(0));
                        String testData = getStringCellValue(row.getCell(3));
//                        String testData = row.getCell(3).toString();

                        if (currentElementName.equals(elementNameToFind)) {
                            // Add the test data to the map
                            testDataMap.put(elementNameToFind, testData);
//                            logger.info("Current Element Name: " + currentElementName);
//                            logger.info("Test Data: " + testData);
                            return testData;
                        }
                    }
                }
            } else {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
*/

   /* private WebElement readExcelToFindElement(String elementNameToFind, String pageName) {
        // Specify the path to your Excel file
        String filePath = currentDirectory + "\\src\\test\\java\\pages\\Pages.xlsx";
        String sheetName = pageName;

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                // Start reading data from the second row (index 1)
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        String currentElementName = getStringCellValue(row.getCell(0));
                        String locatorType = getStringCellValue(row.getCell(1));
                        String locatorValue = getStringCellValue(row.getCell(2));
                        // Find the element based on locator name and value
                        if (currentElementName.equals(elementNameToFind)) {
                            return findElement(currentElementName, locatorType, locatorValue);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
    /*public static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        }
        return "";
    }
*/
    /*private By getBy(String elementName, String locatorType, String locatorValue) {
        switch (locatorType.toLowerCase()) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            // Add more cases as needed (e.g., className, tagName, etc.)
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        }
    }
*/
    /*private WebElement findElement(String elementName, String locatorType, String locatorValue) {
        By by = getBy(elementName, locatorType, locatorValue);
        return baseTest.driver.findElement(by);
        // Wait for the element to be present in the DOM and visible on the page
//        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
*/

    /*private void highlight(WebElement ele) throws IOException {
        //Create object of a JavascriptExecutor interface
        JavascriptExecutor js = (JavascriptExecutor) baseTest.driver;
        //use executeScript() method and pass the arguments
        //Here i pass values based on css style. Yellow background color with cyan color border.
        js.executeScript("arguments[0].setAttribute('style', 'background: cyan; border: 2px solid red;');", ele);
        logger.info("Highlighting the element!");
//        takeScreenshot(baseTest.driver, fileDoc);
    }*/
}
