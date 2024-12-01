package readExcelData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelDataPreviousWorkingVersion {

    // Method to read data from an Excel sheet
//    public static Object[][] readExcel(String filePath, String sheetName) {
//        Object[][] data = null;
//        try (FileInputStream fis = new FileInputStream(filePath)) {
//            Workbook workbook = new XSSFWorkbook(fis);  // For .xlsx files
//            Sheet sheet = workbook.getSheet(sheetName);
//            int rowCount = sheet.getPhysicalNumberOfRows();
//            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();
//
//            data = new Object[rowCount - 1][colCount];  // Create array based on data size
//
//            for (int i = 1; i < rowCount; i++) {  // Start from 1 to skip header row
//                Row row = sheet.getRow(i);
//                for (int j = 0; j < colCount; j++) {
//                    Cell cell = row.getCell(j);
//                    if (cell == null) {
//                        data[i - 1][j] = "";  // If the cell is null, set empty string
//                    } else {
//                        switch (cell.getCellType()) {
//                            case STRING:
//                                data[i - 1][j] = cell.getStringCellValue();
//                                break;
//                            case NUMERIC:
//                                data[i - 1][j] = cell.getNumericCellValue();
//                                break;
//                            case BOOLEAN:
//                                data[i - 1][j] = cell.getBooleanCellValue();
//                                break;
//                            case FORMULA:
//                                data[i - 1][j] = cell.getCellFormula();
//                                break;
//                            default:
//                                data[i - 1][j] = "";  // Default to empty string for any unexpected cell type
//                                break;
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }
	
	public static Object[][] readExcel(String filePath, String sheetName) {
	    Object[][] data = null;
	    try (FileInputStream fis = new FileInputStream(filePath)) {
	        Workbook workbook = new XSSFWorkbook(fis);  // For .xlsx files
	        Sheet sheet = workbook.getSheet(sheetName);
	        int rowCount = sheet.getPhysicalNumberOfRows();
	        int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

	        data = new Object[rowCount - 1][colCount];  // Create array based on data size

	        for (int i = 1; i < rowCount; i++) {  // Start from 1 to skip header row
	            Row row = sheet.getRow(i);
	            for (int j = 0; j < colCount; j++) {
	                Cell cell = row.getCell(j);
	                if (cell == null) {
	                    data[i - 1][j] = "";  // If the cell is null, set empty string
	                } else {
	                    switch (cell.getCellType()) {
	                        case STRING:
	                            data[i - 1][j] = cell.getStringCellValue();
	                            break;
	                        case NUMERIC:
	                            if (DateUtil.isCellDateFormatted(cell)) {
	                                // Handle date-formatted cells
	                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	                                data[i - 1][j] = dateFormat.format(cell.getDateCellValue());
	                            } else {
	                                // Convert numeric value to string to preserve formatting (e.g., for mobile numbers)
	                                data[i - 1][j] = String.valueOf((long) cell.getNumericCellValue());
	                            }
	                            break;
	                        case BOOLEAN:
	                            data[i - 1][j] = String.valueOf(cell.getBooleanCellValue());
	                            break;
	                        case FORMULA:
	                            data[i - 1][j] = cell.getCellFormula();
	                            break;
	                        default:
	                            data[i - 1][j] = "";  // Default to empty string for any unexpected cell type
	                            break;
	                    }
	                }
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return data;
	}


    // Method to write data to a specific column based on column header
    public static void writeToExcel(String filePath, String sheetName, String headerName, Object dataToWrite, int rowIndex) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fis); // For .xlsx files
            Sheet sheet = workbook.getSheet(sheetName);

            // Find the column index based on the header name
            int columnIndex = getColumnIndexByHeader(sheet, headerName);

            if (columnIndex != -1) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) row = sheet.createRow(rowIndex);

                // Write the data in the found column
                Cell cell = row.createCell(columnIndex);
                cell.setCellValue(dataToWrite.toString());
            } else {
                System.out.println("Header not found: " + headerName);
            }

            // Write changes to the file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Helper method to find the column index by header name
    private static int getColumnIndexByHeader(Sheet sheet, String headerName) {
        Row headerRow = sheet.getRow(0);  // Assuming the first row contains the headers
        if (headerRow == null) {
            return -1;  // If header row doesn't exist, return -1
        }

        int colCount = headerRow.getPhysicalNumberOfCells();
        for (int i = 0; i < colCount; i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && cell.getStringCellValue().equalsIgnoreCase(headerName)) {
                return i;  // Return the column index when header matches
            }
        }
        return -1;  // Return -1 if header is not found
    }
}