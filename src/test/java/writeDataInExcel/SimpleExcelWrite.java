package writeDataInExcel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleExcelWrite {

    public static void appendDataToSpecifiedColumn(String filePath, String sheetName, String columnHeader,
                                                   String newValue) throws IOException {
        // Open the existing Excel file
        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet(sheetName);

        // Find the column index of the specified header
        Row headerRow = sheet.getRow(0); // Assuming headers are in the first row
        int columnIndex = -1;
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().equalsIgnoreCase(columnHeader)) {
                columnIndex = cell.getColumnIndex(); // Gets the column index of the specified header
                break;
            }
        }

        if (columnIndex == -1) {
            System.out.println("Column header '" + columnHeader + "' not found!");
            workbook.close();
            file.close();
            return;
        }

        // Append the single value to the matched column
        int lastRowNum = sheet.getLastRowNum(); // Get the last row number
        Row row = sheet.getRow(lastRowNum + 1); // Create a new row after the last row
        if (row == null) {
            row = sheet.createRow(lastRowNum + 1);
        }
        Cell cell = row.createCell(columnIndex); // Add cell in the matched column
        cell.setCellValue(newValue); // Set the new value

        // Write back to the file
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            System.out.println("Value appended successfully to column: " + columnHeader);
        }

        // Close resources
        workbook.close();
        file.close();
    }

    public static void main(String[] args) {
        String filePath = "D:\\Dummy\\UnderstandingExcel\\Book1.xlsx";
        String sheetName = "Sheet1";
        String columnHeader = "password"; // Column header to match
        String newValue = "Berlin"; // Single value to append

        try {
            appendDataToSpecifiedColumn(filePath, sheetName, columnHeader, newValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
