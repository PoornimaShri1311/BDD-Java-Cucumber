package Utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Scenario;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestDataHelper {
    static String currentDirectory = System.getProperty("user.dir");
    static String filePath = currentDirectory + "\\src\\test\\resources\\testData\\testDataFile.xlsx";

    public static String readTestDataFromExcelUtility(String fieldName) {
        Map<String, String> testDataMap = new HashMap<>();
        String sheetName = "Data";
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        String fieldNameExcel = getStringCellValue(row.getCell(0));
                        String testDataExcel = getStringCellValue(row.getCell(1));

                        if (fieldNameExcel.equals(fieldName)) {
                            testDataMap.put(fieldName, testDataExcel);
                            return testDataExcel;
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
}
