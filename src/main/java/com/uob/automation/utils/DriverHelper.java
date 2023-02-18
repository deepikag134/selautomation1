

package com.uob.automation.utils;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverHelper {
	
	/**
	 * Driver Helper
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-13-2019
	 * 
	 */
	private static WebDriver driver;

	public static String getCapabilities(String browser, String value) {
		Capabilities capabilities = ((RemoteWebDriver) getDriver(browser)).getCapabilities();
		if (StringUtils.equalsIgnoreCase(value, "name")) {
			return capabilities.getBrowserName();
		} else if (StringUtils.equalsIgnoreCase(value, "platform")) {
			return capabilities.getPlatform().toString();
		}
		return "Given value is invalid.";
	}


	public static WebDriver getDriver(String browser) {
		@SuppressWarnings("unused")
		DesiredCapabilities capabilities;
		if (driver == null) {
			switch (browser) {
			case "chrome":				
				System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver_109.exe");
				// }
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--headless");
				driver = new ChromeDriver();
				break;
			case "ie":
				System.setProperty("webdriver.ie.driver", "src/main/resources/IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				break;
			case "firefox":
				System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
				driver = new FirefoxDriver();
				break;
			case "safari":
				/***
				 * To use Safari Driver OS should be Mac and Safari should be installed. In
				 * src/main/resources double click on the SafariDriver.safariextz file and
				 * install the extension. Then it can be used.
				 */
				driver = new SafariDriver();
				break;
			default:
				throw new RuntimeException("Specified browser name is not valid." + browser);
			}
		}
		return driver;
	}
}
