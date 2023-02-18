package com.uob.automation.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.google.common.io.Files;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.uob.automation.utils.CommonUtil;
import com.uob.automation.utils.Constants;
import com.uob.automation.utils.DriverHelper;
import com.uob.automation.utils.ExcelReader;
import com.uob.automation.utils.HtmlReport;
import com.uob.automation.utils.PDFReport;

public class BaseTest {
	/**
	 * This class file defines all the generic re-usable methods across any
	 * application.
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-15-2019
	 * 
	 */
	public WebDriver driver;
	public WebDriverWait wait;
	public static CommonUtil util = new CommonUtil();
	public static int TimeoutValue = Integer.valueOf(util.getProperty("TIMEOUT"));
	public static ExtentReports report;
	public static ExtentTest logger;
	public static int FailStepCount = 0;
	public static boolean testStepStatus;
	public static String base64String;
	public static int reportStepCount = 0;
	public static String reportFileName;
	public static String reportTextFileName;
	public static File reportTextFile;
	public static String reportHtmlFileName;
	public static File reportHtmlFile;
	public static String reportPdfFileName;
	public static File reportPdfFile;
	public static File reportExtentFile;
	public static String reportExtentFileName;
	String screenshotName = "";
	String image;
	protected String browser = util.getProperty("BROWSER");

	/**
	 * Creation Extent report and text object before the test suite execution begins.
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-15-2019
	 */
	/**
	 * Integrate TestNG Annotations
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created June-03-2019
	 * 
	 */
	@BeforeSuite
	public void setUp() {
		try {
			createReportFile("SFDC_Test", "txt");
			createReportFile("SFDC_Test", "extent");
			System.out.println("Extent Report : " + reportExtentFileName);
			report = new ExtentReports(reportExtentFileName);
			if (report != null) {
				System.out.println("Extent Report Object Is Created");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Killing tasks before @Test in the Test class File.
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created June-03-2019
	 * 
	 */
	@BeforeTest
	public void baseSetUp() throws IOException {
		Runtime.getRuntime().exec("taskkill /im chromedriver.exe /f");
		Runtime.getRuntime().exec("taskkill /im IEDRIVERSERVER.exe /f");
		if (driver == null) {
			driver = DriverHelper.getDriver(browser);
			wait = new WebDriverWait(driver, 60);
		} else {
			throw new RuntimeException("Driver cannot be null..!!");
		}
	}

	/**
	 * Driver quit after @Test execution
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-15-2019
	 * 
	 */
	@AfterTest
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	/**
	 * Create Reports .pdf, .html, .txt
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-15-2019
	 * 
	 */
	@AfterSuite
	public void createReports() {
		try {
			report.endTest(logger);
			report.flush();
			// report.close();
			Files.createParentDirs(new File(Constants.PdfReportPath + "temp.txt"));
			String pdfReportFileName = Constants.PdfReportPath + reportFileName + ".pdf";
			PDFReport pdf = new PDFReport();
			pdf.generatePDFReport(pdfReportFileName, reportTextFileName);
			String htmlReportFileName = Constants.HtmlReportPath + reportFileName + ".html";
			HtmlReport html = new HtmlReport();
			html.createHtmlFile(htmlReportFileName, reportTextFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch Page URl
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-15-2019a
	 * 
	 */
	public void openPageUrl(String url, String browser, String title) {
		driver.get(util.getProperty(url));
		// This is a temp condition will be removed for sfdc
		if (StringUtils.equalsIgnoreCase(util.getProperty(browser), "firefox")) {
			driver.manage().window().setSize(new Dimension(1600, 900));
		} else {
			driver.manage().window().maximize();
		}
		wait.until(ExpectedConditions.titleIs(title));
		if (!StringUtils.equalsAnyIgnoreCase(driver.getTitle(), title)) {
			throw new RuntimeException(title + " page is not loaded properly.");
		}
	}

	/**
	 * Move to element and click -mouse action
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-15-2019
	 * 
	 */
	public void moveToElementAndClick(WebDriver driver, String strTestCaseName, String deviceDetails,
			String objDescription, By objIdentifier) {
		try {
			WebElement findEle = driver.findElement(objIdentifier);
			Actions actions = new Actions(driver);
			if (findEle != null) {
				actions.moveToElement(findEle);
				actions.click().build().perform();
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Move to Element Position --->"
						+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Move to Element Position --->"
						+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	

	
	
	/**
	 * Escape keyboard key
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @throws IOException 
	 * @created April-15-2019
	 * 
	 */
	public void escapeKeyPressEvent(WebDriver driver, String strTestCaseName, String deviceDetails,
			String objDescription) throws IOException {

			Actions actions = new Actions(driver);
			if (driver== null) {
				
				actions.sendKeys(Keys.ESCAPE).perform();
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Press Escape keyboard event"
						+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Move to Element Position --->"
						+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		}
	
	
	

	/**
	 * Hover the mouse on to the element location
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-16-2019
	 * 
	 */
	public void moveToElementPosition(WebDriver driver, String strTestCaseName, String deviceDetails,
			String objDescription, By objIdentifier) {
		try {
			WebElement findEle = driver.findElement(objIdentifier);
			Actions actions = new Actions(driver);
			if (findEle != null) {
				actions.moveToElement(findEle);
				actions.build().perform();
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Move to Element Position --->"
						+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Move to Element Position --->"
						+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**Label Info
	 * 
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created June-4-2019
	 * 
	 */
	public String addLabelInfo(String label) {
		return "<span class='info label'>" + label + "</span>";
	}

	/**
	 * Report Label
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-16-2019
	 * 
	 */
	public void addSuccessLabelToReport(WebDriver driver, String strTestCaseName) throws IOException {
		String logReport = strTestCaseName + Constants.DELIMITER + "" + Constants.DELIMITER + getCurrentDateAndTime()
				+ Constants.DELIMITER + addLabelSuccess(strTestCaseName.toUpperCase()) + Constants.DELIMITER + "NO DATA"
				+ Constants.DELIMITER + Constants.stepPass;
		appendReportFile(driver, logReport);
		extentReport(driver, logReport);
	}

	/**
	 * add label
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-23-2019
	 * 
	 */
	public String addLabelSuccess(String label) {
		return "<span class='success label'>" + label + "</span>";
	}

	/**
	 * PDF - log start test - Do not delete
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-23-2019
	 * 
	 */
	public void startTest(WebDriver driver, String strTestCaseName, String strDevice) throws IOException {
		String logReport = strTestCaseName + Constants.DELIMITER + strDevice + Constants.DELIMITER
				+ getCurrentDateAndTime() + Constants.DELIMITER
				+ addLabelInfo("/****Start of execution****/".toUpperCase()) + Constants.DELIMITER + "NO DATA"
				+ Constants.DELIMITER + Constants.stepPass;
		appendReportFile(driver, logReport);
		extentReport(driver, logReport);
	}

	/**
	 * PDF - log end test - Do not delete 
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-23-2019
	 * 
	 */
	public void endTest(WebDriver driver, String strTestCaseName, String strDevice) throws IOException {
		String logReport = strTestCaseName + Constants.DELIMITER + strDevice + Constants.DELIMITER
				+ getCurrentDateAndTime() + Constants.DELIMITER + addLabelSuccess("TEST IS COMPLETED")
				+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
		appendReportFile(driver, logReport);
		extentReport(driver, logReport);
	}

	/**
	 * get current date and time
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-24-2019
	 * 
	 */
	public String getCurrentDateAndTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return sdf.format(cal.getTime());
	}

	/**
	 * Verify if the element exists
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	public boolean isElementPresent(WebDriver driver, By by) throws InterruptedException {
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName()+ "------->" + testStepStatus);
		boolean elementFound = true;
		int findLoop = 0;
		Thread.sleep(1000);
		while (elementFound) {
			
			Thread.sleep(1000);
			try {
				if (driver.findElement(by).isDisplayed()) {
					return true;
				}
			} catch (Exception e) {
				findLoop++;
			}
			if (findLoop >= 59) {
				break;
			}
		}
		return false;
	}
	
	/**
	 * In this method we can specify the wait time for a specific object by calling this method
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created June-06-2019
	 * 
	 */
	
public boolean isElementPresent(WebDriver driver, By by, String objDescription, int waitTime) throws InterruptedException {
		
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName()+ "------->" + testStepStatus);

		if (!testStepStatus) {
			return false;
		}

		boolean elementFound = false;
		int findLoop = 0;

		while (!elementFound) {
			Thread.sleep(1000);
			
			if (findLoop > waitTime) {
				break;
			}
			
			System.out.println("*************************************************************************");
			System.out.println("Trying to find the element : " + objDescription + "----> " + findLoop + " ("  + by.toString() +  ")");
			System.out.println("*************************************************************************");
			
			try {
				if (driver.findElement(by).isDisplayed()) {
					return true;
				} else {
					findLoop++;
				}
			} catch (Exception e) {
				findLoop++;
			}
		}
		return false;
	}


/**
 * In this method we find the element presence by object description
 * 
 * @author DeepikaGuduru
 * @version 1.0
 * @created June-06-2019
 * 
 */

public boolean isElementPresent(WebDriver driver, By by, String objDescription) throws InterruptedException {
	
	System.out.println(new Object(){}.getClass().getEnclosingMethod().getName()+ "------->" + testStepStatus);

	if (!testStepStatus) {
		return false;
	}

	boolean elementFound = false;
	int findLoop = 0;

	while (!elementFound) {
		Thread.sleep(1000);
		if (findLoop > Constants.MaxWaitTimeInSec) {
			break;
		}
		System.out.println("******************************************************************************************************************");
		System.out.println("Trying to find the element : " + objDescription + "----> " + findLoop + " ("  + by.toString() +  ")");
		System.out.println("******************************************************************************************************************");
		try {
			if (driver.findElement(by).isDisplayed()) {
				System.out.println("******************************************************************************************************************");
				System.out.println("Found the element : " + objDescription + "----> " + findLoop + " ("  + by.toString() +  ")");
				System.out.println("******************************************************************************************************************");
				return true;
			} else {
				findLoop++;
			}
		} catch (Exception e) {
			findLoop++;
		}
	}
	return false;
}


	/**
	 * Method for storing web elements - In progress
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	public WebElement findElement(WebDriver driver, By objIdentifier) throws InterruptedException {
		boolean findEle = isElementPresent(driver, objIdentifier);
		WebElement element;
		if (findEle) {
			List<WebElement> objElement = null;
			objElement = driver.findElements(objIdentifier);
			Iterator<WebElement> it = objElement.iterator();
			while (it.hasNext()) {
				element = it.next();
				if (element.isDisplayed() && element.isEnabled()) {
					System.out.println("Found The Element : " + objIdentifier.toString());
					return element;
				}
			}
		}
		return null;
	}

	/**
	 * get the text of a web element
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	public String getObjectText(WebDriver driver, By objIdentifier) throws InterruptedException {
		WebElement findEle = findElement(driver, objIdentifier);
		WebElement element;
		String returnText = null;
		if (findEle != null) {
			List<WebElement> objElement = null;
			objElement = driver.findElements(objIdentifier);
			Iterator<WebElement> it = objElement.iterator();
			while (it.hasNext()) {
				element = it.next();
				if (element.isDisplayed() && element.isEnabled()) {
					return element.getText();
				}
			}
		}
		return returnText;
	}

	/**
	 * Switch to the window
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	public void switchWindow(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			String index) throws IOException {
		if (StringUtils.isNotEmpty(index)) {
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(Integer.valueOf(index)));
			String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
					+ getCurrentDateAndTime() + Constants.DELIMITER + "Switched to Window --->" + objDescription
					+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
			appendReportFile(driver, logReport);
			extentReport(driver, logReport);
		} else {
			String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
					+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Switched to Window --->" + objDescription
					+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
			appendReportFile(driver, logReport);
			extentReport(driver, logReport);
		}
	}

	/**
	 * Click the element by using Xpath
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	public void clickElementByXpath(WebDriver driver, String strTestCaseName, String deviceDetails,
			String objDescription, By objIdentifier) throws IOException {
		WebElement findEle = driver.findElement(objIdentifier);
		if (findEle != null) {
			findEle.click();
			String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
					+ getCurrentDateAndTime() + Constants.DELIMITER + "Click Link--->" + objDescription
					+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
			appendReportFile(driver, logReport);
			extentReport(driver, logReport);
		} else {
			String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
					+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Click Link--->" + objDescription
					+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
			appendReportFile(driver, logReport);
			extentReport(driver, logReport);
		}
	}

	/**
	 * Click the link text
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	public void clickLinkText(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			String objIdentifier) throws IOException {
		WebElement findEle = driver.findElement(By.linkText(objIdentifier));
		if (findEle != null) {
			findEle.click();
			String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
					+ getCurrentDateAndTime() + Constants.DELIMITER + "Click Link--->" + objDescription
					+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
			appendReportFile(driver, logReport);
			extentReport(driver, logReport);
		} else {
			String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
					+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Click Link--->" + objDescription
					+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
			appendReportFile(driver, logReport);
			extentReport(driver, logReport);
		}
	}

	/**
	 * Hover to the element
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-02-2019
	 * 
	 */
	public void hoverOnElement(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			By objIdentifier) {
		try {
			WebElement findEle = findElement(driver, objIdentifier);
			if (findEle != null) {
				Actions action = new Actions(driver);
				wait.until(ExpectedConditions.visibilityOf(findEle));
				action.moveToElement(findEle).build().perform();
				action.moveToElement(findEle).click().perform();
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Click Link--->" + objDescription
						+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Click Link--->" + objDescription
						+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Click on the link
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	public void clickLink(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			By objIdentifier) {
		try {
			WebElement findEle = findElement(driver, objIdentifier);
			if (findEle != null) {
				findEle.click();
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Click Link--->" + objDescription
						+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Click Link--->" + objDescription
						+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Click Button Text
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-03-2019
	 * 
	 */
	public void clickButtontText(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			String objIdentifier) {
		try {
			WebElement findEle = driver.findElement(By.xpath("//button[text()='" + objIdentifier + "']"));
			if (findEle.isDisplayed()) {
				findEle = driver.findElement(By.xpath("//button[text()='" + objIdentifier + "']"));
			} else {
				findEle = driver.findElement(By.xpath("//button[contains(text(),'" + objIdentifier + "')]"));
			}
			if (findEle != null) {
				findEle.click();
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Click Button--->" + objDescription
						+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Click Button--->" + objDescription
						+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Click Button
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-03-2019
	 * 
	 */
	public void clickButton(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			By objIdentifier) {
		try {
			WebElement findEle = findElement(driver, objIdentifier);
			if (findEle != null) {
				findEle.click();
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Click Button--->" + objDescription
						+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Click Button--->" + objDescription
						+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Click on the Image - In Progress
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-03-2019
	 * 
	 */
	public void clickImage(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			By objIdentifier) {
		try {
			WebElement findEle = findElement(driver, objIdentifier);
			if (findEle != null) {
				findEle.click();
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Click Image--->" + objDescription
						+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not  Click Image--->" + objDescription
						+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Select Value from the list
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-03-2019
	 * 
	 */
	public void selectValue(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			By objIdentifier, String value) {
		try {
			WebElement findEle = findElement(driver, objIdentifier);
			if (findEle != null) {
				Select oSelection = new Select(findEle);
				List<WebElement> allValues = oSelection.getOptions();
				for (int i = 0; i < allValues.size(); i++) {
					String listValue = allValues.get(i).getText();
					if (listValue.equalsIgnoreCase(value))
						findEle.sendKeys(listValue);
					String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
							+ getCurrentDateAndTime() + Constants.DELIMITER + "Select Value--->" + objDescription
							+ Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepPass;
					appendReportFile(driver, logReport);
					extentReport(driver, logReport);
					break;
				}
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Select Value--->" + objDescription
						+ Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Select Check box
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-03-2019
	 * 
	 */
	public void selectCheckbox(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			By objIdentifier, String value) {
		try {
			WebElement findEle = findElement(driver, objIdentifier);
			if (findEle != null) {
				if (value.equalsIgnoreCase("off")) {
					if (findEle.isSelected())
						findEle.click();
					String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
							+ getCurrentDateAndTime() + Constants.DELIMITER + "Select Checkbox--->" + objDescription
							+ Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepPass;
					appendReportFile(driver, logReport);
					extentReport(driver, logReport);
				} else if (value.equalsIgnoreCase("on")) {
					if (!findEle.isSelected())
						findEle.click();
					String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
							+ getCurrentDateAndTime() + Constants.DELIMITER + "Select Checkbox--->" + objDescription
							+ Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepPass;
					appendReportFile(driver, logReport);
					extentReport(driver, logReport);
				}
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not Select Checkbox--->" + objDescription
						+ Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Select Radio Button
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-03-2019
	 * 
	 */
	public void selectRadioButton(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			By objIdentifier, String value) {
		try {
			WebElement findEle = findElement(driver, objIdentifier);
			if (findEle != null) {
				findEle.click();
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Select RadioButton--->" + objDescription
						+ Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not  Select RadioButton--->"
						+ objDescription + Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Set Password encrypted
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-03-2019
	 * 
	 */
	public void setPassword(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			By objIdentifier, byte[] value) {
		// System.out.println("Inisde the Method - setValue");
		try {
			WebElement findEle = findElement(driver, objIdentifier);
			if (findEle != null) {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Set Value--->" + objDescription
						+ Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
				findEle.clear();
				findEle.sendKeys(util.getDecryptedMessage(value));
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not  Set Value--->" + objDescription
						+ Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Clear and enter the text
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-03-2019
	 * 
	 */
	public void setValue(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			By objIdentifier, String value) {
		// System.out.println("Inisde the Method - setValue");
		try {
			WebElement findEle = findElement(driver, objIdentifier);
			if (findEle != null) {
				findEle.clear();
				findEle.sendKeys(value);
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Set Value--->" + objDescription
						+ Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Did Not  Set Value--->" + objDescription
						+ Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Click on the text element
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-03-2019
	 * 
	 */
	public void checkElementText(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			By objIdentifier, String value) {
		String logReport = null;
		try {
			WebElement findEle = driver.findElement(objIdentifier);
			if (findEle != null) {
				String text = findEle.getText().trim();
				if (StringUtils.equalsIgnoreCase(value, text)) {
					logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
							+ getCurrentDateAndTime() + Constants.DELIMITER + "Actual Text matches Expected Text --->"
							+ objDescription + Constants.DELIMITER + value + Constants.DELIMITER + Constants.stepPass;
				} else {
					logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
							+ getCurrentDateAndTime() + Constants.DELIMITER
							+ "Actual Text doesn't matches Expected Text --->" + objDescription + Constants.DELIMITER
							+ value + Constants.DELIMITER + Constants.stepFail;
				}
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Verify if element is displayed
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-06-2019
	 * 
	 */
	public boolean VerifyElementIsDisplayed(WebDriver driver, String strTestCaseName, String deviceDetails,
			String objDescription, By objIdentifier) throws IOException {
		boolean returnValue = false;
		try {
			List<WebElement> objElement = driver.findElements(objIdentifier);
			int objCount = objElement.size();
			if (objCount > 0) {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Verification: Element/Object Displayed---> "
						+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
				returnValue = true;
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER
						+ "Verification: Element/Object NOT Displayed---> " + objDescription + Constants.DELIMITER
						+ "NO DATA" + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
				returnValue = false;
			}
		} catch (Exception e) {
			String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
					+ getCurrentDateAndTime() + Constants.DELIMITER + "Verification: Element/Object NOT Displayed--->"
					+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
			appendReportFile(driver, logReport);
			extentReport(driver, logReport);
			e.printStackTrace();
			returnValue = false;
		}
		return returnValue;
	}

	/**
	 * Verify if the element is not displayed
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-06-2019
	 * 
	 */
	public boolean VerifyElementIsNotDisplayed(WebDriver driver, String strTestCaseName, String deviceDetails,
			String objDescription, By objIdentifier) throws IOException {
		boolean returnValue = false;
		try {
			// WebElement findEle = findElement(driver, objIdentifier);
			Thread.sleep(10000);
			List<WebElement> objElement = driver.findElements(objIdentifier);
			int objCount = objElement.size();
			if (objCount == 0) {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER
						+ "Verification: Element/Object Not Displayed--->" + objDescription + Constants.DELIMITER
						+ "NO DATA" + Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
				returnValue = true;
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER
						+ "Verification: Element/Object Is Displayed--->" + objDescription + Constants.DELIMITER
						+ "NO DATA" + Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
				returnValue = false;
			}
		} catch (Exception e) {
			String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
					+ getCurrentDateAndTime() + Constants.DELIMITER + "Verification: Element/Object  Not Displayed--->"
					+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
			appendReportFile(driver, logReport);
			extentReport(driver, logReport);
			e.printStackTrace();
			returnValue = false;
		}
		return returnValue;
	}

	/**
	 * For PDF report - Do not delete - In progress
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-06-2019
	 * 
	 */
	@SuppressWarnings("unchecked")
	static void setWebDriver(WebDriver driver) {
		((ThreadLocal<WebDriver>) driver).set(driver);
	}

	/**
	 * get the screenshot name
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-06-2019
	 * 
	 */
	public String getScreenshotName() {
		return Constants.ScreenshotsPath + getCurrentDateAndTime() + ".jpg";
	}

	/**
	 * Append Screenshots to reports - Do not delete or modify
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-10-2019
	 * 
	 */
	public void appendReportFile(WebDriver driver, String logInfo) throws IOException {
		// Write Content
		screenshotName = getScreenshotName();
		captureScreenShot(driver, screenshotName);
		logInfo = logInfo.replaceAll("\\<.*?>", "") + Constants.DELIMITER + screenshotName;
		FileWriter writer = new FileWriter(reportTextFile, true);
		BufferedWriter bw = new BufferedWriter(writer);
		PrintWriter pw = new PrintWriter(bw);
		pw.println(logInfo);
		pw.close();
	}

	/**
	 * Capture Screenshots 
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-10-2019
	 * 
	 */
	public void captureScreenShot(WebDriver driver, String fileName) {
		try {
			// Take screenshot and store as a file format
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			// now copy the screenshot to desired location using copyFile method //
			FileUtils.copyFile(src, new File(fileName));
			// Capture the image as base64 image
			base64String = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Close the report file once writing the steps after suite
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-10-2019
	 * 
	 */
	public void closeReportFile(String logInfo) throws IOException {
		// Write Content
		FileWriter writer = new FileWriter(reportTextFile);
		writer.close();
	}

	/**
	 * Create Report file for .txt
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-10-2019
	 * 
	 */
	public String createReportFile(String strTestCaseName, String fileType) {
		String returnFileName = null;
		reportFileName = strTestCaseName + "_" + getCurrentDateAndTime();
		if (fileType.equalsIgnoreCase("txt")) {
			try {
				reportTextFileName = Constants.TextReportPath + reportFileName + ".txt";
				returnFileName = reportTextFileName;
				reportTextFile = new File(reportTextFileName);
				Files.createParentDirs(reportTextFile);
				System.out.println("File is created!");
				// if (reportTextFile.createNewFile()) {
				// } else {
				// System.out.println("File already exists.");
				// }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (fileType.equalsIgnoreCase("html")) {
			try {
				reportHtmlFileName = Constants.HtmlReportPath + reportFileName + ".html";
				returnFileName = reportHtmlFileName;
				reportHtmlFile = new File(reportTextFileName);
				if (reportHtmlFile.createNewFile()) {
					System.out.println("File is created!");
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (fileType.equalsIgnoreCase("pdf")) {
			try {
				reportPdfFileName = Constants.PdfReportPath + reportFileName + ".pdf";
				returnFileName = reportPdfFileName;
				reportPdfFile = new File(reportTextFileName);
				if (reportPdfFile.createNewFile()) {
					System.out.println("File is created!");
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (fileType.equalsIgnoreCase("extent")) {
			reportExtentFileName = Constants.ExtentReportPath + reportFileName + ".html";
			// returnFileName = reportExtentFileName;
			// reportExtentFile = new File(reportTextFileName);
			//
			// if (reportExtentFile.createNewFile()) {
			// System.out.println("File is created!");
			// } else {
			// System.out.println("File already exists.");
			// }
		}
		return returnFileName;
	}

	/**
	 * GMethod returns today's date ( Ex: 2)
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	public String getTodaysDate() {
		Calendar cal = Calendar.getInstance();
		return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * // Method returns the day of the week ( Ex: Wednesday)
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	
	public String getTodaysDay() {
		DayOfWeek dayOfWeek = DayOfWeek.from(LocalDate.now());
		return dayOfWeek.toString();
		// String daysArray[] = {"Sunday","Monday","Tuesday",
		// "Wednesday","Thursday","Friday", "Saturday"};
		// Calendar calendar = Calendar.getInstance();
		// return daysArray[calendar.get(Calendar.DAY_OF_WEEK)];
	}

	/**
	 * get current month
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	// Method returns the current month ( Ex: August)
	public String getCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		// Calendar cal = Calendar.getInstance();
		// String[] monthNames = {"January", "February", "March", "April", "May",
		// "June", "July", "August", "September", "October", "November", "December"};
		// return monthNames[cal.get(Calendar.MONTH)];
	}

	/**
	 * Method returns the current year ( Ex: 2017)
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	public String getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return String.valueOf(cal.get(Calendar.YEAR));
	}

	/**
	 * Print
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-20-2019
	 * 
	 */
	public void reportTestCase(WebDriver driver, String strTestScriptName, String deviceDetails,String JIRATestCaseName, String testCaseDetails) throws IOException {
		FailStepCount = 0;
		String logReport = strTestScriptName + Constants.DELIMITER + deviceDetails+ Constants.DELIMITER+ getCurrentDateAndTime()
				+ Constants.DELIMITER + "Test Case --->" +JIRATestCaseName+ Constants.DELIMITER + testCaseDetails + Constants.DELIMITER
				+ Constants.stepPass;
		appendReportFile(driver, logReport);
		extentReport(driver, logReport);
	}

	
	
	/**
	 * Method returns the current month and year ( Ex: August 2017)
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */

	public String getCurrentMonthYear() {
		Calendar cal = Calendar.getInstance();
		String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		String year = String.valueOf(cal.get(Calendar.YEAR));
		return month + " " + year;
	}

	/**
	 * Method returns the current month and date ( Ex: August 2)
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-01-2019
	 * 
	 */
	public String getCurrentMonthDate() {
		Calendar cal = Calendar.getInstance();
		String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		String date1 = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		return month + " " + date1;
	}

	/**
	 * Compare Values - In progress
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-22-2019
	 * 
	 */
	public void compareValues(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription,
			String expectedValue, String actualValue) {
		// System.out.println("Inisde the Method - setValue");
		try {
			if (expectedValue.equalsIgnoreCase(actualValue)) {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Compare Values Of The Object--->"
						+ objDescription + Constants.DELIMITER + ("Exp: " + expectedValue + "-->Act: " + actualValue)
						+ Constants.DELIMITER + Constants.stepPass;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			} else {
				String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
						+ getCurrentDateAndTime() + Constants.DELIMITER + "Compare Values  Of The Object--->"
						+ objDescription + Constants.DELIMITER + ("Exp: " + expectedValue + "-->Act: " + actualValue)
						+ Constants.DELIMITER + Constants.stepFail;
				appendReportFile(driver, logReport);
				extentReport(driver, logReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Extent reports
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-20-2019
	 * 
	 */
	public void extentReport(WebDriver driver, String logInfo) {
		try {
			// image = logger.addScreenCapture(screenshotName);
			String[] getValue = logInfo.split("@@@@@");
			// String testCaseName = getValue[0];
			// String deviceName = getValue[1];
			// String DateTime = getValue[2];
			String stepDescription = getValue[3];
			String stepValue = getValue[4];
			String stepStatus = getValue[5];
			// String screenshot = getValue[6];
			if (stepValue.equals("NO DATA"))
				stepValue = "";
			if (StringUtils.equalsAnyIgnoreCase(stepStatus, "pass")) {
				if (Constants.ScreenshotsToAllStepsInExtentReport) {
					if (stepValue != "") {
						logger.log(LogStatus.PASS, stepDescription + "-----> " + stepValue,
								logger.addBase64ScreenShot("data:image/png;base64," + base64String));
					} else {
						logger.log(LogStatus.PASS, stepDescription,
								logger.addBase64ScreenShot("data:image/png;base64," + base64String));
					}
					// logger.log(LogStatus.PASS, stepDescription,
					// logger.addScreenCapture(screenshotName));
				} else {
					logger.log(LogStatus.PASS, stepDescription, stepValue);
				}
			} else if (stepStatus.equalsIgnoreCase("FAIL")) {
				if (stepValue != "") {
					logger.log(LogStatus.FAIL, stepDescription + "-----> " + stepValue,
							logger.addBase64ScreenShot("data:image/png;base64," + base64String));
				} else {
					logger.log(LogStatus.FAIL, stepDescription,
							logger.addBase64ScreenShot("data:image/png;base64," + base64String));
				}
				// logger.log(LogStatus.FAIL, stepDescription,
				// logger.addScreenCapture(screenshotName));
			} else {
				logger.log(LogStatus.INFO, stepDescription, stepValue);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Reading Excel execution sheet 
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-22-2019
	 * 
	 */
	public String getExecutionRows(String strTestCaseName) {
		String getTestCaseName = "";
		String executionRows = "";
		String getRunMode = "N";
		int totalRows = 0;
		int totalExecutionCount = 0;
		String ExecutionFileName = Constants.ExecuteFileLocation;
		ExcelReader xlsReadExecutionFile = new ExcelReader(ExecutionFileName);
		totalRows = xlsReadExecutionFile.getRowCount(Constants.ExecuteFileSheet);
		for (int rowLoop = 1; rowLoop <= totalRows; rowLoop++) {
			getTestCaseName = xlsReadExecutionFile.getCellData(Constants.ExecuteFileSheet, "TestCaseName", rowLoop);
			if (getTestCaseName.equalsIgnoreCase(strTestCaseName)) {
				getRunMode = xlsReadExecutionFile.getCellData(Constants.ExecuteFileSheet, "RunFlag", rowLoop);
				if (getRunMode.equalsIgnoreCase("Y") || getRunMode.equalsIgnoreCase("Yes")) {
					totalExecutionCount++;
					executionRows = executionRows + "," + rowLoop;
				}
			}
		}
		executionRows.trim();
		return executionRows.substring(1);
	}

	/**
	 * Getting the cell data from the execution sheet
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-20-2019
	 * 
	 */
	public String getDataFromExecutionExcel(int row, String colName) {
		String getValue = "";
		ExcelReader excel = new ExcelReader(Constants.ExecuteFileLocation);
		getValue = excel.getCellData(Constants.ExecuteFileSheet, colName, row);
		return getValue;
	}

	/**
	 * The method will return the total number of rows from the execution file. This
	 * count will be used to iterate through the test script
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-20-2019
	 * 
	 */

	public int getRowsFromExecutionFile() {
		int totalRows = 0;
		String ExecutionFileName = Constants.ExecuteFileLocation;
		ExcelReader xlsReadExecutionFile = new ExcelReader(ExecutionFileName);
		totalRows = xlsReadExecutionFile.getRowCount(Constants.ExecuteFileSheet);
		return totalRows;
	}

	/**
	 * The method is used to verify the run flag of a particular test script from
	 * the execution file If the run flag is "Y" or "Yes", it'll return true value
	 * to the called method Otherwise, false value will be returned to the called
	 * method
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-30-2019
	 * 
	 */
	
	public boolean verifyExecutionRunMode(String strTestCaseName) {
		boolean testCaseFound = false;
		String getTestCaseName = "";
		String getRunMode = "N";
		int totalRows = 0;
		String ExecutionFileName = Constants.ExecuteFileLocation;
		ExcelReader xlsReadExecutionFile = new ExcelReader(ExecutionFileName);
		totalRows = xlsReadExecutionFile.getRowCount(Constants.ExecuteFileSheet);
		for (int rowLoop = 1; rowLoop <= totalRows; rowLoop++) {
			getTestCaseName = xlsReadExecutionFile.getCellData(Constants.ExecuteFileSheet, "TestCaseName", rowLoop);
			if (getTestCaseName.equalsIgnoreCase(strTestCaseName)) {
				testCaseFound = true;
				getRunMode = xlsReadExecutionFile.getCellData(Constants.ExecuteFileSheet, "RunFlag", rowLoop);
				if (getRunMode.equals("Y")) {
					getRunMode = "YES";
				}
				break;
			}
		}
		if (testCaseFound && getRunMode.equalsIgnoreCase("YES")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * The method is used to get the values from the execution file based on the
	 * column.
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-13-2019
	 * 
	 */
	
	public String getExecutionData(String strTestCaseName, String colName) {
		String getTestCaseName = "";
		int totalRows = 0;
		String returnData = "";
		String ExecutionFileName = Constants.ExecuteFileLocation;
		ExcelReader xlsReadExecutionFile = new ExcelReader(ExecutionFileName);
		totalRows = xlsReadExecutionFile.getRowCount(Constants.ExecuteFileSheet);
		for (int rowLoop = 1; rowLoop <= totalRows; rowLoop++) {
			getTestCaseName = xlsReadExecutionFile.getCellData(Constants.ExecuteFileSheet, "TestCaseName", rowLoop);
			if (getTestCaseName.equalsIgnoreCase(strTestCaseName)) {
				returnData = xlsReadExecutionFile.getCellData(Constants.ExecuteFileSheet, colName, rowLoop);
				break;
			}
		}
		return returnData;
	}

	/**
	 * Getting the row count from the input file
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-13-2019
	 * 
	 */
	public int getRowsFromInputFile(String inputFileName, String sheetName) {
		ExcelReader xlsReadExecutionFile = new ExcelReader(inputFileName);
		return xlsReadExecutionFile.getRowCount(sheetName);
	}

	/**
	 * Reading excel cell data
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-13-2019
	 * 
	 */
	public String getDataFromInputFile(String fileName, String sheetName, String colName, int rowNo) {
		ExcelReader excel = new ExcelReader(fileName);
		return excel.getCellData(sheetName, colName, rowNo);
	}

	/**
	 * Reading from Excel
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-13-2019
	 * 
	 */
	@DataProvider(name = "GetRecords", parallel = true)
	public static Object[][] sauceBrowserDataProvider(Method testMethod) {
		String ExecutionFileName = Constants.ExecuteFileLocation;
		ExcelReader xls = new ExcelReader(ExecutionFileName);
		int totalRows = xls.getRowCount(Constants.ExecuteFileSheet);
		String sheet = Constants.ExecuteFileSheet;
		Object[][] records = new Object[totalRows][8];
		int counter = 0;
		for (int i = 0; i < totalRows; i++) {
			records[counter++] = new Object[] { xls.getCellData(sheet, "TestCaseName", i + 1),
					xls.getCellData(sheet, "DeviceModel", i + 1), xls.getCellData(sheet, "DeviceName", i + 1),
					xls.getCellData(sheet, "PlatformName", i + 1), xls.getCellData(sheet, "PlatformVersion", i + 1),
					xls.getCellData(sheet, "RunFlag", i + 1) };
		}
		return records;
	}

	@DataProvider(name = "getExecutionData")
	public Object[][] executionData() {
		Object[][] arrayObject = getExecutionExcelData();
		return arrayObject;
	}

	/**
	 * Get Execution Excel data- In progress
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created June-03-2019
	 * 
	 */
	public String[][] getExecutionExcelData() {
		String[][] arrayExcelData = null;
		try {
			String ExecutionFileName = Constants.ExecuteFileLocation;
			ExcelReader xls = new ExcelReader(ExecutionFileName);
			int totalRows = xls.getRowCount(Constants.ExecuteFileSheet);
			int totalCols = xls.getColumnCount(Constants.ExecuteFileSheet);
			arrayExcelData = new String[totalRows - 1][totalCols];
			for (int row = 1; row < totalRows; row++) {
				for (int col = 0; col < totalCols; col++) {
					arrayExcelData[row - 1][col] = xls.getCellData(Constants.ExecuteFileSheet, col, row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayExcelData;
	}

	/**
	 * get data from input file login sheet
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-20-2019
	 * 
	 */
	public String getDataFromInputFileLoginSheet(String fileName, String sheetName, String roleColName, int rowNo,
			String returnColName) throws IOException {
		String getValue = null;
		String getLoginRole;
		String getDataRole;
		ExcelReader excel = new ExcelReader(fileName);
		if (!excel.isSheetExist(sheetName)) {
			reportFail(getWebDriver(), fileName, sheetName, "Check Data Sheet Exist",
					"Data Sheet Does Not Exist ---> " + sheetName);
			abortTest(getWebDriver());
		}
		getDataRole = excel.getCellData(sheetName, roleColName, rowNo);
		// int totalRows = excel.getRowCount(sheetName);
		int totalRows = excel.getRowCount("Login");
		for (int row = 2; row <= totalRows; row++) {
			getLoginRole = excel.getCellData("Login", Constants.RoleColumn, row);
			if (getLoginRole.equalsIgnoreCase(getDataRole)) {
				getValue = excel.getCellData("Login", returnColName, row);
				break;
			}
		}
		return getValue;
	}

	/**
	 * Driver quit after @Test execution
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-13-2019
	 * 
	 */
	public void quitDriver() {
		if (getWebDriver() != null) {
			((WebDriver) driver).quit();
			driver = null;
		}
	}

	/**
	 * @return the {@link WebDriver} for the current thread
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-13-2019
	 * 
	 */

	public WebDriver getWebDriver() {
		return driver;
	}

	/**
	 * Abort test
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-15-2019
	 * 
	 */
	public void abortTest(WebDriver driver) {
		try {
			//
			// report.endTest(logger);
			// report.flush();
			// report.close();
			String pdfReportFileName = Constants.PdfReportPath + reportFileName + ".pdf";
			PDFReport pdf = new PDFReport();
			pdf.generatePDFReport(pdfReportFileName, reportTextFileName);
			// String htmlReportFileName = Constants.HtmlReportPath + reportFileName +
			// ".html";
			// HtmlReport html = new HtmlReport();
			// html.createHtmlFile(htmlReportFileName, reportTextFileName);
			driver.quit();
			testStepStatus = false;
			FailStepCount++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report Fail
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-29-2019
	 * 
	 */
	public void reportFail(WebDriver driver, String strTestCaseName, String strDevice, String actionName,
			String objDescription) throws IOException {
		actionName = actionName.toLowerCase();
		if (!actionName.contains("verification")) {
			testStepStatus = false;
		}
		FailStepCount++;
		String logReport = strTestCaseName + Constants.DELIMITER + strDevice + Constants.DELIMITER
				+ getCurrentDateAndTime() + Constants.DELIMITER + actionName + "--->" + objDescription
				+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
		appendReportFile(driver, logReport);
		extentReport(driver, logReport);
	}
	// public WebDriver getWebDriver() {
	// return driver.get();
	// }
	
	
	
	
	public void reportPass(WebDriver driver, String strTestCaseName, String deviceDetails, String actionName,
			String objDescription) throws IOException {
		String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
				+ getCurrentDateAndTime() + Constants.DELIMITER + actionName + "--->"
				+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
		appendReportFile(driver, logReport);
		extentReport(driver, logReport);
	}
	
	public void reportInfo(WebDriver driver, String strTestCaseName, String strDevice, String actionName,
			String objDescription) throws IOException {
		
		if (!testStepStatus) {
			return;
		}
		String logReport = strTestCaseName + Constants.DELIMITER + strDevice + Constants.DELIMITER
				+ getCurrentDateAndTime() + Constants.DELIMITER + actionName + "--->"
				+ objDescription + Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepInfo;
		appendReportFile(driver, logReport);
		extentReport(driver, logReport);
	}
	

	/**
	 * return execution row
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-29-2019
	 * 
	 */
	public String[] returnExecutionRows(String executionRows) {
		String[] arrRowExecute = null;
		String getRow = null;
		String exeRow = "";
		String returnRows[] = null;
		if (executionRows.contains(",")) {
			arrRowExecute = executionRows.split(",");
			for (int rowLoop = 0; rowLoop < arrRowExecute.length; rowLoop++) {
				getRow = arrRowExecute[rowLoop];
				if (getRow.contains("-")) {
					String[] splitRow = getRow.split("-");
					int startRow = Integer.parseInt(splitRow[0]);
					int endRow = Integer.parseInt(splitRow[1]);
					for (int addRow = startRow; addRow <= endRow; addRow++) {
						exeRow = exeRow + addRow + ",";
					}
				} else {
					exeRow = exeRow + getRow + ",";
				}
				returnRows = exeRow.split(",");
			}
		} else if (executionRows.contains("-")) {
			String[] splitRow = executionRows.split("-");
			int startRow = Integer.parseInt(splitRow[0]);
			int endRow = Integer.parseInt(splitRow[1]);
			for (int addRow = startRow; addRow <= endRow; addRow++) {
				exeRow = exeRow + addRow + ",";
			}
			exeRow = exeRow.substring(0, exeRow.length() - 1);
			exeRow.trim();
			returnRows = exeRow.split(",");
		} else {
			exeRow = executionRows;
			returnRows = exeRow.split("");
		}
		System.out.println(exeRow);
		for (int execLoop = 0; execLoop < returnRows.length; execLoop++) {
			System.out.println(returnRows[execLoop]);
		}
		return returnRows;
	}

	/**
	 * Copy with Channels
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * 
	 * 
	 */
	public void copyWithChannels(File aSourceFile, File aTargetFile, boolean aAppend) throws IOException {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		try {
			try {
				inStream = new FileInputStream(aSourceFile);
				inChannel = inStream.getChannel();
				outStream = new FileOutputStream(aTargetFile, aAppend);
				outChannel = outStream.getChannel();
				long bytesTransferred = 0; // defensive loop - there's usually only a single
				iteration: while (bytesTransferred < inChannel.size()) {
					bytesTransferred += inChannel.transferTo(0, inChannel.size(), outChannel);
				}
			} finally { // being
				// defensive about closing all channels and streams
				if (inChannel != null)
					inChannel.close();
				if (outChannel != null)
					outChannel.close();
				if (inStream != null)
					inStream.close();
				if (outStream != null)
					outStream.close();
			}
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
		}
	}

	/**
	 * Delete Folder directory
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-20-2019
	 * 
	 */
	public void deleteFolder(File folder) throws IOException {
		// System.out.println("Folder Name: "+folder.toString() );
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					// deleteFolder(f);
					FileUtils.deleteDirectory(f);
				} else {
					// f.delete();
				}
			}
		}
		// folder.delete();
	}
}
