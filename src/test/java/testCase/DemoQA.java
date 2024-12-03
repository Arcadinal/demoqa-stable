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
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import helper.HelperMethods;
import readExcelData.ReadExcelData1;


public class DemoQA {

	String filePath = "./src/test/resources/PersonDetails.xlsx";
	String sheetName = "Sheet1";
	WebDriver driver;
	String mainWindow = null;

	@BeforeMethod
	public void setup() {
		EdgeOptions opt = new EdgeOptions();

		// Added Extension to block ads from the site
		opt.addExtensions(new File("./crx Extension/Adblocker.crx"));

		driver = new EdgeDriver(opt);
		driver.manage().window().maximize();
		driver.get("https://demoqa.com/");

		// Getting the window Handle of the first launched browser tab (Which is
		// demoqa).
		mainWindow = driver.getWindowHandle();

	}

	 @Test(dataProvider = "demoqa", dataProviderClass = DataProviders.class,priority = 1)
	public void Fillthetextboxform(String username, String email, String gender, Object mobileNumber, Object dob,
			String subjects, String currentAddress, String Output1, String Output2, int rowIndex) {

		// Switch and closing window
		Set<String> windowHandles = driver.getWindowHandles();
		for (String handle : windowHandles) {
			driver.switchTo().window(handle);

			if (!driver.getCurrentUrl().contains("demoqa.com")) {
				driver.close();
			}
		}

		// Switching back to the demoqa tab
		driver.switchTo().window(mainWindow);

		try {
			WebElement element = driver.findElement(By.xpath("//h5[.='Elements']"));
			HelperMethods.scrollIntoView(driver, element);
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

		//Giving the name of the column header as Output1 to append value in that column
		ReadExcelData1.writeToExcel(filePath, sheetName, "Output1", dynamicOutput, rowIndex);

	}

	@Test(dataProvider = "demoqa", dataProviderClass = DataProviders.class,priority = 2)
	public void FillthePracticeform(String username, String email, String gender, String mobileNumber, String dob,
			String subjects, String currentAddress, String Output1, String Output2, int rowIndex)
			throws InterruptedException {

		Set<String> windowHandles = driver.getWindowHandles();
		for (String handle : windowHandles) {
			driver.switchTo().window(handle);

			if (!driver.getCurrentUrl().contains("demoqa.com")) {
				driver.close();
			}
		}

		driver.switchTo().window(mainWindow);

		System.out.println("Filling form...");

		String mobileStr = mobileNumber != null ? String.valueOf(mobileNumber) : "";

		String dobStr = dob != null ? String.valueOf(dob) : "";

		driver.findElement(By.xpath("(//div[@class='card mt-4 top-card'])[2]")).click();

		driver.findElement(By.xpath("//span[.='Practice Form']")).click();
		
		//Use Assert to check the title of form 
		WebElement headingPracticeForm = driver.findElement(By.xpath("//h1[.='Practice Form']"));
		String heading = headingPracticeForm.getText();
		Assert.assertEquals(heading,"Practice Form");

		String full_name[] = username.split(" ");
		String firstName = full_name[0].trim();
		String lastName = full_name[1].trim();

		driver.findElement(By.xpath("//input[@id='firstName']")).sendKeys(firstName);
		driver.findElement(By.xpath("//input[@id='lastName']")).sendKeys(lastName);
		driver.findElement(By.xpath("//input[@id='userEmail']")).sendKeys(email);

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

		driver.findElement(By.xpath("//input[@id='userNumber']")).sendKeys(mobileStr);

		//Parsing the Date to String
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			Date date = sdf.parse(dobStr); 
			String formattedDate = sdf.format(date);

			WebElement dates = driver.findElement(By.xpath("//input[@id='dateOfBirthInput']"));
			dates.click();

			// Clearing the Date Field Before data input
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value = '';", dates);
			dates.sendKeys(formattedDate);
			dates.sendKeys(Keys.ENTER);

		} catch (ParseException e) {
			System.out.println("Date format issue: " + e.getMessage());
		}

		// SUBJECTS
		String[] Subjects = subjects.split(",");

		WebElement txtSubjects = driver.findElement(By.xpath("//input[@id='subjectsInput']"));
		HelperMethods.scrollIntoView(driver, txtSubjects);

		for (String subs : Subjects) {
			txtSubjects.sendKeys(subs);
			txtSubjects.sendKeys(Keys.ENTER);
		}

		driver.findElement(By.xpath("//textarea[@id='currentAddress']")).sendKeys(currentAddress);

		String[] address = currentAddress.split(",");
		String state = address[address.length - 1].trim();
		String city = address[address.length - 2].trim();

		// For State dropdown
		WebElement txtState = driver.findElement(By.id("state"));
		HelperMethods.scrollIntoView(driver, txtState);
		txtState.click();

		driver.findElement(By.xpath("//div[text()='" + state + "']")).click(); // Change the state as needed

		// For City dropdown
		WebElement cityField = driver.findElement(By.id("city"));
		HelperMethods.scrollIntoView(driver, cityField);
		cityField.click();

		driver.findElement(By.xpath("//div[text()='" + city + "']")).click(); // Change the city as needed

		WebElement btnSubmit = driver.findElement(By.xpath("//button[@id='submit']"));
		HelperMethods.scrollIntoView(driver, btnSubmit);
		btnSubmit.click();

		WebElement titleofForm = driver.findElement(By.xpath("//div[@id='example-modal-sizes-title-lg']"));
		String thanksMSsg = titleofForm.getText();

		WebElement closeBtn = driver.findElement(By.xpath("//button[@id='closeLargeModal']"));
		HelperMethods.scrollIntoView(driver, closeBtn);
		closeBtn.click();

		ReadExcelData1.writeToExcel(filePath, sheetName, "Output2", thanksMSsg, rowIndex);

	}

	@AfterMethod
	public void flush() {
		if (driver != null) {
			driver.quit();
		}
	}

}
