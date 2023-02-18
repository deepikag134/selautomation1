package com.uob.automation.pages;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.uob.automation.common.BaseTest;
import com.uob.automation.utils.Constants;

public class SalesForceLoginPage extends BaseTest {
//	private By username = By.id("username");
	private By password = By.id("password");
	private By loginBtn = By.id("Login");
	private By username = By.name("username");

	public SalesForceLoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, TimeoutValue), this);
		wait = new WebDriverWait(driver, TimeoutValue);
	}

	public void loginWith(String testcaseName, String details, String userName, String passWord, String userId)
			throws Exception {
		// loadPageUrl();
		loaduobPageUrl(driver, testcaseName, details, "for uob is launched");
		setValue(driver, testcaseName, details, "Enter Valid Username", username, userName);
//		setPassword(driver, testcaseName, details, "Enter Valid Password", password,
//				util.getEncryptedMessage(passWord));
//		clickButton(driver, testcaseName, details, "Click on login button", loginBtn);
	}

	private void loadPageUrl() {
		driver.get(util.getProperty("BASE_URL"));
		// This is a temp condition will be removed once new gecko is added
		if (StringUtils.equalsIgnoreCase(util.getProperty("BROWSER"), "firefox")) {
			driver.manage().window().setSize(new Dimension(1600, 900));
		} else {
			driver.manage().window().maximize();
		}
		wait.until(ExpectedConditions.titleIs("UOB : United Overseas Bank"));
		if (!StringUtils.equalsAnyIgnoreCase(driver.getTitle(), "UOB : United Overseas Bank")) {
			throw new RuntimeException("LoginPage is not loaded properly.");
		}
	}

	public void loaduobPageUrl(WebDriver driver, String strTestCaseName, String deviceDetails, String objDescription)
			throws IOException {
		try {
			driver.get(util.getProperty("BASE_URL"));
			driver.manage().window().maximize();
			String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
					+ getCurrentDateAndTime() + Constants.DELIMITER + "Page URL  --->" + objDescription
					+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepPass;
			appendReportFile(driver, logReport);
			extentReport(driver, logReport);
		} catch (Exception e) {
			e.printStackTrace();
			String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
					+ getCurrentDateAndTime() + Constants.DELIMITER + "Did not load the Page URL" + objDescription
					+ Constants.DELIMITER + "NO DATA" + Constants.DELIMITER + Constants.stepFail;
			appendReportFile(driver, logReport);
			extentReport(driver, logReport);
		}
		wait.until(ExpectedConditions.titleIs("UI CRUD Operations"));
		String logReport = strTestCaseName + Constants.DELIMITER + deviceDetails + Constants.DELIMITER
				+ getCurrentDateAndTime() + Constants.DELIMITER + "Page title is Verified" + Constants.DELIMITER
				+ "NO DATA" + Constants.DELIMITER + Constants.stepPass;
		appendReportFile(driver, logReport);
		extentReport(driver, logReport);
		if (!StringUtils.equalsAnyIgnoreCase(driver.getTitle(), "UI CRUD Operations")) {
			throw new RuntimeException("LoginPage is not loaded properly.");
		}
	}
}
