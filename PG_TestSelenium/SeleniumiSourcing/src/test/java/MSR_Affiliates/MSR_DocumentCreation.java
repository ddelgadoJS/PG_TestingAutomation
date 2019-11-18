package MSR_Affiliates;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import components.Credentials;
import io.github.bonigarcia.wdm.WebDriverManager;

public class MSR_DocumentCreation {
	static String browser = "Chrome";
	static WebDriver driver;
	
	public static WebDriver getWebDriver(String browser) {
		if (browser.equals("Chrome")) {
			WebDriverManager.chromedriver().setup();
			return driver = new ChromeDriver();
		} else if (browser.equals("Firefox")) {
			WebDriverManager.firefoxdriver().setup();
			return driver = new FirefoxDriver();
		} else if (browser.equals("Edge")) {
			WebDriverManager.edgedriver().setup();
			return driver = new EdgeDriver();
		} else if (browser.equals("Internet Explorer")) {
			WebDriverManager.iedriver().setup();
			return driver = new InternetExplorerDriver();
		}
		return null;
	}

	public static void waitReportDownload(WebDriver driver) throws InterruptedException {
		TimeUnit.SECONDS.sleep(10);
		while (driver.findElement(By.id("loaderText")).isDisplayed())
			TimeUnit.SECONDS.sleep(10);
	}
	
	public static void captureScreenshot() throws IOException {
		Date d = new Date();
		String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
		
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File(".//screenshots//" + fileName));
	}
	
	public static void main(String[] args) throws InterruptedException {
		Credentials credentials = new Credentials();
		
		driver = getWebDriver(browser);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);

		WebDriverWait explicitWait = new WebDriverWait(driver, 60);
		
		FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver);
		fluentWait.withTimeout(Duration.ofSeconds(60));
		fluentWait.pollingEvery(Duration.ofMillis(600));
		fluentWait.withMessage("Timed out after 20 seconds.");
		fluentWait.ignoring(NoSuchElementException.class);
		
		driver.get("https://k8a.na.pg.com:8415/irj/portal/jes");
		
		driver.findElement(By.xpath("//table[@class='urLogonTable']//table//input[1]")).click();
		
		driver.findElement(By.xpath("/html[1]/body[1]/table[1]/tbody[1]/tr[1]/td[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[3]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/div[1]/div[2]/span[2]")).click();
	}
}
