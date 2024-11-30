package testCase;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import readExcelData.ReadExcelDataPreviousWorkingVersion;

public class TEstes {

	WebDriver driver;
	String filePath = "D:\\Dummy\\UnderstandingExcel\\Book1.xlsx";
	String sheetName = "Sheet1";

	@BeforeMethod
	public void setup() {
		driver = new EdgeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.saucedemo.com/");
	}

	@Test(dataProvider = "saucelabs", dataProviderClass = DataProviders.class)
	public void ddtLogin(String username, String password, String Output1,int rowIndex) {
		driver.findElement(By.id("user-name")).sendKeys(username);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("login-button")).click();
		
		
		// Check to see if login was success
		if (driver.getCurrentUrl().contains("inventory")) {
			ReadExcelDataPreviousWorkingVersion.writeToExcel(filePath, sheetName,"Output1","Success",rowIndex);
		} else {
			ReadExcelDataPreviousWorkingVersion.writeToExcel(filePath, sheetName,"Output1","Failed",rowIndex);
		}

		// Introducing ExplicitWait to solve elementNotInteractable Exception

		driver.findElement(By.xpath("//button[contains(text(),'Open Menu')]")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='logout_sidebar_link']"))).click();

	}

	@AfterMethod
	public void flush() {
		if (driver != null) {
			driver.quit();
		}
	}

}
