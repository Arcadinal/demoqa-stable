package readExcelData;

import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {

	public static void main(String[] args) throws IOException {
		String fileLocation = "D:\\Dummy\\UnderstandingExcel\\Book1.xlsx";
		XSSFWorkbook wb = new XSSFWorkbook(fileLocation);
		XSSFSheet sh = wb.getSheetAt(0);
		for (int i = 1; i < sh.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = sh.getRow(i);
			for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
				XSSFCell cell = row.getCell(j);
				DataFormatter dft = new DataFormatter();
				String value = dft.formatCellValue(cell);
			//	String value = cell.getStringCellValue();
				System.out.println(value);
			} 
		}
		
		wb.close();
	}

}
