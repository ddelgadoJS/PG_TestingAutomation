package testcases;
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

import io.github.bonigarcia.wdm.WebDriverManager;

public class AllocationReport {
	public static String browser = "Chrome";
	public static String downloadFilePath = "C:\\Users\\delgado.jd.6\\Downloads\\TemporalSeleniumDownloads";
	public static WebDriver driver;
	public static String user, password = "";
	
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

	public static ChromeOptions setChromeOptions(String downloadFilePath) {
		// Set download file path and automatic downloads.
		// TO-DO. FIND OUT WHY IT'S NOT FINDING THE ELEMENTS.
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilePath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		return options;
	}
	
	public static void captureScreenshot() throws IOException {
		Date d = new Date();
		String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
		
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File(".//screenshots//" + fileName));
	}
	
	public static void main(String[] args) throws InterruptedException {
		driver = getWebDriver(browser);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		WebDriverWait explicitWait = new WebDriverWait(driver, 20);
		
		FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver);
		fluentWait.withTimeout(Duration.ofSeconds(60));
		fluentWait.pollingEvery(Duration.ofMillis(600));
		fluentWait.withMessage("Timed out after 20 seconds.");
		fluentWait.ignoring(NoSuchElementException.class);
		
		driver.get("https://isourcingqa.pg.com/");
		
		// 0. Login page.
		driver.findElement(By.id("username")).sendKeys(user); // Username box
		driver.findElement(By.id("password")).sendKeys(password); // Password box
		driver.findElement(By.id("loginButton")).click(); // Login button
		
		// 1. Initial iSourcing screen, select My Reports option.
		// To wait for PingID. Delete if there are credentials that don't require it.
		explicitWait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("My Reports"))).click(); // My Reports tab
		driver.findElement(By.xpath("//a[@ng-click='RedirectToAllocationReport()']")).click(); // Allocation report
		
		// 2. Inside Allocation Report screen, create a NNIT report.
		driver.findElement(By.name("reportName")).sendKeys("NNIT"); // Report name text field
		TimeUnit.SECONDS.sleep(2);
		driver.findElement(By.xpath("/html/body/div[10]/div[1]/div[2]/div[1]/div[1]/div/button")).click(); // Create report button
		TimeUnit.SECONDS.sleep(2);
		driver.findElement(By.linkText("Output Report Fields")).click(); // Output Report Fields button

		// 2.1 Testing buttons: ADD, ADD ALL, REMOVE, REMOVE ALL.
		WebElement availableFieldsElement = driver.findElement(By.xpath("//select[@ng-model='selectedAvailableField']"));
		WebElement selectedFieldsElement = driver.findElement(By.xpath("//select[@ng-model='selectedSelectFields']"));
		Select availableFieldsSelect = new Select(availableFieldsElement);
		Select selectedFieldsSelect = new Select(selectedFieldsElement);
		
		// ADD first element and then ADD ALL elements of Available Fields list.
		availableFieldsSelect.selectByValue("0");
		driver.findElement(By.xpath("//button[@ng-click='addToSelectedColumn()']")).click(); // ADD button
		driver.findElement(By.xpath("//button[@ng-click='moveAll(availableColumns,selectedColumns)']")).click(); // ADD ALL button
		
		// REMOVE first element and then REMOVE ALL elements from Selected Fields list.
		selectedFieldsSelect.selectByValue("0");
		driver.findElement(By.xpath("//button[@ng-click='removeFromSelectedColumn()']")).click(); // REMOVE button
		driver.findElement(By.xpath("//button[@ng-click='moveAll(selectedColumns,availableColumns)']")).click(); // REMOVE ALL button
		
		// 2.2 ADD one element and save template.
		availableFieldsSelect.selectByValue("0");
		driver.findElement(By.xpath("//button[@ng-click='addToSelectedColumn()']")).click(); // ADD button
		driver.findElement(By.xpath("//input[@ng-click='triggerSaveTemplate()']")).click(); // Save Template button
		
		explicitWait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[@id='indexSuccessModal']//button[contains(text(),'OK')]"))).click(); // Success pop-up OK button
		TimeUnit.SECONDS.sleep(2);
		driver.findElement(By.xpath("//input[@ng-click='triggerDownloadReport()']")).click(); // Download Report button
		
		waitReportDownload(driver);
		
		driver.quit();
	}
}
