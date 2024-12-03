package helper;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HelperMethods {
	
	public static void scrollIntoView(WebDriver driver,WebElement element) {
		((JavascriptExecutor)driver)
		.executeScript("arguments[0].scrollIntoView({behaviour:'smooth',block: 'center'});",element);
	}
	
	public  static void scrollIntoViewAndClick(WebDriver driver,By locator) {
		WebElement element = driver.findElement(locator);
		scrollIntoView(driver,element);
		element.click();
	}
	
	

}
