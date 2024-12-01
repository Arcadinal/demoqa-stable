package switchTabswithAdBlockerExtensionsOn;

import java.io.File;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.Test;

public class SwitchTabWithAdBlockerExtension {

	WebDriver driver;
	
	@Test
	public void testWindowSwitch() throws InterruptedException {
		EdgeOptions opt = new EdgeOptions();
		opt.addExtensions(new File("./crx Extension/Adblocker.crx"));
		driver = new EdgeDriver(opt);
		driver.manage().window().maximize();
		
		driver.get("https://www.google.com");
		String mainWindowHandle = driver.getWindowHandle();
		
		Thread.sleep(2000);
		
		Set<String> windowHandles = driver.getWindowHandles();
//		for (String windowHandle : windowHandles) {
//			driver.switchTo().window(windowHandle);
//			
//			if(driver.getCurrentUrl().contains("google")) {
//				System.out.println("Switched to Google");
//				break;
//			}
//			
//		}
//		System.out.println(windowHandles);
		
		for(String handle:windowHandles) {
			driver.switchTo().window(handle);
			
			if(!driver.getCurrentUrl().contains("google.com")) {
				driver.close();
			}
		}
		
		driver.switchTo().window(mainWindowHandle);
		
	}
}
