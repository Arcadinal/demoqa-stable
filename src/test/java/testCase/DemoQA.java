package testCase;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import readExcelData.ReadExcelDataPreviousWorkingVersion;

public class DemoQA {
	
	String filePath = "./src/test/resources/PersonDetails.xlsx";
	String sheetName = "Sheet1";
	WebDriver driver;

	@BeforeMethod
	public void setup() {
	
		driver = new EdgeDriver();
		driver.manage().window().maximize();
		driver.get("https://demoqa.com/");
		
		
		
	}
	
	@Test(dataProvider = "demoqa", dataProviderClass = DataProviders.class)
	public void Fillthetextboxform(String username,String email,String currentAddress,String Output1,int rowIndex) {
		
		WebElement element = driver.findElement(By.xpath("//h5[.='Elements']"));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		element.click();
		
		WebElement txtBox = driver.findElement(By.xpath("//span[.='Text Box']"));
		txtBox.click();
		
		 driver.findElement(By.xpath("//input[@id='userName']")).sendKeys(username);
		 driver.findElement(By.xpath("//input[@id='userEmail']")).sendKeys(email);
		 driver.findElement(By.xpath("//textarea[@id='currentAddress']")).sendKeys(currentAddress);
		 driver.findElement(By.xpath("//textarea[@id='permanentAddress']")).sendKeys(currentAddress);
	
		 driver.findElement(By.xpath("//button[@id='submit']")).click();
		 
		 String dynamicOutput = driver.findElement(By.xpath("//div[@id='output']")).getText();
		 
		 ReadExcelDataPreviousWorkingVersion.writeToExcel(filePath, sheetName,"Output1",dynamicOutput,rowIndex);
		
	
	}
	

	
	@AfterMethod
	public void flush() {
		if (driver != null) {
			driver.quit();
		}
	}

}
