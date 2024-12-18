package testCase;

import java.util.Arrays;

import org.testng.annotations.DataProvider;

import readExcelData.ReadExcelData1;


public class DataProviders {

	
	@DataProvider(name = "demoqa")
	public Object[][] getExcelDatas(){
		String filePath = "./src/test/resources/PersonDetails.xlsx";
		String sheetName = "Sheet1";
		Object[][]data = ReadExcelData1.readExcel(filePath, sheetName);
	
		for (int i = 0; i < data.length; i++) {
	        data[i] = Arrays.copyOf(data[i], data[i].length + 1); // Extend the array
	        data[i][data[i].length - 1] = i + 1; // Add row index as the last element
	       
//	        data[i][3] = String.valueOf(data[i][3]); // Trying Convert mobileNumber (Double) to String
//	        data[i][4] = String.valueOf(data[i][4]);

	    }
	    return data;
	
	}
}
