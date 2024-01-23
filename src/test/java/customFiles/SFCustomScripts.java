package customFiles;

import Utilities.ConfigReader;
import Utilities.TestDataHelper;
import base.BaseTest;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

@Getter
public class SFCustomScripts extends BaseTest {
    private static final Logger logger = Logger.getLogger(SFCustomScripts.class.getName());
    //    static BaseTest baseTest = new BaseTest();
    private static BaseTest baseTest;
    private static Map<String, WebElement> elementMap = new HashMap<>();

    public SFCustomScripts(WebDriver driver) {
//        this.baseTest = baseTest;
        BaseTest.driver = driver;
    }

    public static void selectDataFromDD(String arg0, String arg1, String arg2) throws IOException {
        WebElement element = readExcelToFindElementwithMap(arg1, arg2);
        if (element != null) {
            highlight(element);
            Select dd = new Select(element);
            dd.selectByValue(arg0);
        }
    }

    public static void waitForElementInPage(String arg0, String arg1, String arg2, String continueOnFailure) {
        WebDriverWait wait = new WebDriverWait(baseTest.driver, Duration.ofSeconds(30));
        WebElement element = readExcelToFindElementwithMap(arg0, arg2);
        try {
            if (arg1.equals("displayed")) {
                WebElement Eelement = wait.until(ExpectedConditions.visibilityOf(element));
                highlight(element);
                logger.info("Element is displayed in " + arg2);
            } else {
                logger.info("Element is not displayed");
                handleFailure(continueOnFailure);
            }
        } catch (Exception e) {
            logger.info("Exception occurred while clicking the element: " + e.getMessage());
            handleFailure(continueOnFailure);
        }
    }

    public static void enterDataForOutline(String fieldValue, String elementName, String pageName, String continueOnFailure) {
        WebElement element = readExcelToFindElementwithMap(elementName, pageName);
        try {
            if (element != null) {
                highlight(element);
                element.clear();
                element.sendKeys(fieldValue);
            } else {
                handleFailure(continueOnFailure);
            }

        } catch (Exception e) {
            e.printStackTrace();
            handleFailure(continueOnFailure);
        }
    }

    public static void generateRandomValueAndStore(String arg0, String arg1, String arg2) {
        WebElement element = readExcelToFindElementwithMap(arg1, arg2);
//        int randomNo = generateRandomValue();
//        String readNumber = writeIntoExcel(arg1,arg2);
        if (element != null) {
            element.clear();
            element.sendKeys(String.valueOf(generateRandomValue(arg0)));
        }
    }

    private static Object generateRandomValue(String arg0) {
        Random r = new Random();
        Object randomValue = null;
        if (arg0.equals("long")) {
            long randomTenDigitNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
            randomValue = randomTenDigitNumber;
        } else if (arg0.equals("int")) {
            int randomTenDigitNumber = r.nextInt(100) + 1;
            randomValue = randomTenDigitNumber;
        }

        return randomValue;
    }

    public static void readAndStore(String arg0, String arg1, String arg2) throws IOException {
        WebElement element = readExcelToFindElementwithMap(arg1, arg2);
        if (element != null) {
            highlight(element);
            String elementValue;
            if (arg0.equals("Attribute")) {
                elementValue = element.getAttribute("value");
                String readNumber = writeIntoExcel(elementValue, arg1, arg2);
            } else if (arg0.equals("text")) {
                elementValue = element.getText();
                String readNumber = writeIntoExcel(elementValue, arg1, arg2);
            }
        }
    }

    private static String writeIntoExcel(String readValue, String elementNameToFind, String pageName) {
//        
        String currentDirectory = System.getProperty("user.dir");
        String filePath = currentDirectory + ConfigReader.getProperty("pageObjectsPath");
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

                        if (currentElementName.equals(elementNameToFind)) {
                            // Modify the existing code to write data into the Excel file
                            Cell testDataCell = row.createCell(3);  // Assuming column index 3 for test data
                            testDataCell.setCellValue(readValue);  // Replace with the new test data

                            // Save the changes to the Excel file
                            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                                workbook.write(fileOut);
                            }

                            logger.info("Test data written for element: " + elementNameToFind);
                            break;  // Assuming you want to stop after finding the first match
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

    public static void verifyIfElementDisplayed(String elementName, String visibility, String pageName, String continueOnFailure) {
        WebElement element = readExcelToFindElementwithMap(elementName, pageName);
        try {
            // Check visibility based on the provided condition
            if (visibility.equalsIgnoreCase("displayed")) {
                // Verify if the element is displayed
                assert element != null;
                if (element.isDisplayed()) {
                    highlight(element);
                    logger.info("Element '" + elementName + "' is displayed.");
                } else {
                    handleFailure(continueOnFailure);
                }
            } else if (visibility.equalsIgnoreCase("not displayed")) {
                // Verify if the element is not displayed
                assert element != null;
                if (!element.isDisplayed()) {
                    logger.info("Element '" + elementName + "' is not displayed.");
                } else {
                    handleFailure(continueOnFailure);
                }
            }
        } catch (Exception e) {
            logger.info("Exception occurred while clicking the element: " + e.getMessage());
            handleFailure(continueOnFailure);
        }
    }

    public static void launchURL(String arg1) {
        String homepageUrl = ConfigReader.getProperty(arg1);
        System.out.println(homepageUrl);
        baseTest.driver.get(homepageUrl);
        logger.info(arg1 + " is launched as expected!");
    }

    public void setUp() throws IOException {
//        BaseTest baseTest = new BaseTest();
        baseTest.setup();
    }

    public static void enterDataInTestField(String testData, String elementName, String pageName, String continueOnFailure) {
        WebElement element = readExcelToFindElementwithMap(elementName, pageName);
        try {
            if (element != null) {
                String elementValue = TestDataHelper.readTestDataFromExcelUtility(testData);
                element.clear();
                element.sendKeys(elementValue);
                highlight(element);
                logger.info(elementName + " is filled with " + testData);
            } else {
                System.out.println("Element not found: " + elementName);
                handleFailure(continueOnFailure);
            }
        } catch (Exception e) {
            System.out.println("Exception occurred while clicking the element: " + e.getMessage());
            handleFailure(continueOnFailure);
        }
    }

    public static void clickButton(String elementName, String pageName) throws IOException {
        WebElement element = readExcelToFindElementwithMap(elementName, pageName);
        if (element != null) {
            highlight(element);
            element.click();
        } else {
            System.out.println("Element not found: " + elementName);
        }
    }

    private static void highlight(WebElement ele) throws IOException {
        //Create object of a JavascriptExecutor interface
        JavascriptExecutor js = (JavascriptExecutor) baseTest.driver;
        //use executeScript() method and pass the arguments
        //Here i pass values based on css style. Yellow background color with cyan color border.
        js.executeScript("arguments[0].setAttribute('style', 'background: cyan; border: 2px solid red;');", ele);
        logger.info("Highlighting the element!");
    }

    private static WebElement readExcelToFindElementwithMap(String elementNameToFind, String pageName) {
//        
        String currentDirectory = System.getProperty("user.dir");
        String filePath = currentDirectory + ConfigReader.getProperty("pageObjectsPath");
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

    public static String getStringCellValue(Cell cell) {
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

    private static By getBy(String elementName, String locatorType, String locatorValue) {
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

    private static WebElement findElement(String elementName, String locatorType, String locatorValue) {
        By by = getBy(elementName, locatorType, locatorValue);
        return baseTest.getDriver().findElement(by);
    }

    public static void buttonClick(String elementName, String pageName, String continueOnFailure) {
        WebElement element = readExcelToFindElementwithMap(elementName, pageName);
        try {
            if (element != null || element.isDisplayed()) {
                highlight(element);

                element.click();
                logger.info(elementName + " clicked!");
            } else {
                logger.info("Element not found: " + elementName);
                handleFailure(continueOnFailure);
            }
        } catch (Exception e) {
            logger.info("Exception occurred while clicking the element: " + e.getMessage());
            handleFailure(continueOnFailure);
        }
    }

    private static void handleFailure(String continueOnFailure) {
        if ("(Continue on Failure)".equalsIgnoreCase(continueOnFailure)) {
            logger.info("Continue on failure. Test execution continues.");
            logger.info("Handling Failure!");
        } else {
            throw new AssertionError("Element not found, and continueOnFailure is not specified.");
        }
    }
}
