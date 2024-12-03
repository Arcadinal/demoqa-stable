package testCase;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import helper.HelperMethods;
import readExcelData.ReadExcelDataPreviousWorkingVersion;

public class DemoQA {

	String filePath = "./src/test/resources/PersonDetails.xlsx";
	String sheetName = "Sheet1";
	WebDriver driver;
	String mainWindow = null;


	@BeforeMethod
	public void setup() {
		EdgeOptions opt = new EdgeOptions();
		opt.addExtensions(new File("./crx Extension/Adblocker.crx"));
		driver = new EdgeDriver(opt);
		driver.manage().window().maximize();
		driver.get("https://demoqa.com/");
		mainWindow = driver.getWindowHandle();
		

	}

	//@Test(dataProvider = "demoqa", dataProviderClass = DataProviders.class)
	public void Fillthetextboxform(String username, String email, String gender, Object mobileNumber, Object dob,
			String subjects, String currentAddress, String Output1, String Output2, int rowIndex) {

		Set<String> windowHandles = driver.getWindowHandles();
		for(String handle:windowHandles) {
			driver.switchTo().window(handle);
			
			if(!driver.getCurrentUrl().contains("demoqa.com")) {
				driver.close();
			}
		}
		
		driver.switchTo().window(mainWindow);
		
		try {
			WebElement element = driver.findElement(By.xpath("//h5[.='Elements']"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
		} catch (NoSuchElementException e) {
		    System.out.println("Element not found: " + e.getMessage());
		}
		
		

		WebElement txtBox = driver.findElement(By.xpath("//span[.='Text Box']"));
		txtBox.click();

		driver.findElement(By.xpath("//input[@id='userName']")).sendKeys(username);
		driver.findElement(By.xpath("//input[@id='userEmail']")).sendKeys(email);
		driver.findElement(By.xpath("//textarea[@id='currentAddress']")).sendKeys(currentAddress);
		driver.findElement(By.xpath("//textarea[@id='permanentAddress']")).sendKeys(currentAddress);

		
		
		WebElement btnSubmit = driver.findElement(By.xpath("//button[@id='submit']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", btnSubmit);
		btnSubmit.click();

		String dynamicOutput = driver.findElement(By.xpath("//div[@id='output']")).getText();

		ReadExcelDataPreviousWorkingVersion.writeToExcel(filePath, sheetName, "Output1", dynamicOutput, rowIndex);

	}

	@Test(dataProvider = "demoqa", dataProviderClass = DataProviders.class)
	public void FillthePracticeform(String username, String email, String gender, String mobileNumber, String dob,
			String subjects, String currentAddress, String Output1, String Output2, int rowIndex) throws InterruptedException {
		
		Set<String> windowHandles = driver.getWindowHandles();
		for(String handle:windowHandles) {
			driver.switchTo().window(handle);
			
			if(!driver.getCurrentUrl().contains("demoqa.com")) {
				driver.close();
			}
		}
		
		driver.switchTo().window(mainWindow);
		
		System.out.println("Filling form...");

		String mobileStr = mobileNumber != null ? String.valueOf(mobileNumber) : "";

		String dobStr = dob != null ? String.valueOf(dob) : "";

		driver.findElement(By.xpath("(//div[@class='card mt-4 top-card'])[2]")).click();
		
		driver.findElement(By.xpath("//span[.='Practice Form']")).click();

		String full_name[] = username.split(" ");
		String firstName = full_name[0].trim();
		String lastName = full_name[1].trim();
		
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@id='firstName']")).sendKeys(firstName);
		driver.findElement(By.xpath("//input[@id='lastName']")).sendKeys(lastName);

		driver.findElement(By.xpath("//input[@id='userEmail']")).sendKeys(email);
		Thread.sleep(1000);

		switch (gender) {

		case "Male":
			driver.findElement(By.xpath("//label[.='Male']")).click();
			break;
		case "Female":
			driver.findElement(By.xpath("//label[.='Female']")).click();
			break;
		default:
			driver.findElement(By.xpath("//label[.='Other']")).click();
			break;
		}
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@id='userNumber']")).sendKeys(mobileStr);
		
		Thread.sleep(1000);
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); 
//		String formattedDate = sdf.format(dob);  // Format the Date to String
//		driver.findElement(By.xpath("//input[@id='dateOfBirthInput']")).sendKeys(formattedDate);

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			Date date = sdf.parse(dobStr); // Try parsing the date
			String formattedDate = sdf.format(date);
			
			WebElement dates = driver.findElement(By.xpath("//input[@id='dateOfBirthInput']"));
			dates.click();
			
			//Clearing the Date Field Before data input
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value = '';", dates);
			dates.sendKeys(formattedDate);
			dates.sendKeys(Keys.ENTER);
			
			//Thread.sleep(2000);
		} catch (ParseException e) {
			System.out.println("Date format issue: " + e.getMessage());
		}
		
		
		//SUBJECTS
		String [] Subjects = subjects.split(",");
		
		WebElement txtSubjects = driver.findElement(By.xpath("//input[@id='subjectsInput']"));
		HelperMethods.scrollIntoView(driver, txtSubjects);
		for (String subs : Subjects) {
			txtSubjects.sendKeys(subs);
			txtSubjects.sendKeys(Keys.ENTER);
		}
		
		
		//Thread.sleep(1000);
		driver.findElement(By.xpath("//textarea[@id='currentAddress']")).sendKeys(currentAddress);
		//Thread.sleep(1000);

		String[] address = currentAddress.split(",");
		String state = address[address.length - 1].trim();
		String city = address[address.length - 2].trim();

		// For State dropdown
		WebElement txtState = driver.findElement(By.id("state"));
		HelperMethods.scrollIntoView(driver, txtState);
		txtState.click();
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//div[text()='" + state + "']")).click(); // Change the state as needed
		Thread.sleep(1000);

		// For City dropdown
		WebElement cityField = driver.findElement(By.id("city"));
		HelperMethods.scrollIntoView(driver, cityField);
		cityField.click();
		
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//div[text()='" + city + "']")).click(); // Change the city as needed
		Thread.sleep(1000);

		WebElement btnSubmit = driver.findElement(By.xpath("//button[@id='submit']"));
		HelperMethods.scrollIntoView(driver, btnSubmit);
		btnSubmit.click();
		
		
		Thread.sleep(1000);

		WebElement titleofForm = driver.findElement(By.xpath("//div[@id='example-modal-sizes-title-lg']"));
		String thanksMSsg = titleofForm.getText();

		WebElement closeBtn = driver.findElement(By.xpath("//button[@id='closeLargeModal']"));
		HelperMethods.scrollIntoView(driver, closeBtn);
		closeBtn.click();

		ReadExcelDataPreviousWorkingVersion.writeToExcel(filePath, sheetName, "Output2", thanksMSsg, rowIndex);

	}
	
	

	@AfterMethod
	public void flush() {
		if (driver != null) {
			driver.quit();
		}
	}

}
