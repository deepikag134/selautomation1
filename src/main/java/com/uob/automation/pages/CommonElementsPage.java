package com.uob.automation.pages;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.uob.automation.common.BaseTest;

public class CommonElementsPage extends BaseTest {
	
	private WebDriverWait wait;
	


	By UserNavLabel = By.xpath("//span[@id='userNavLabel']");
	By CloseAllPrimarytabs = By.xpath("//span[contains(text(),'Close all primary tabs')]");
	By Arrow = By.xpath("//div[@class='x-tab-tabmenu-right x-tab-tabmenu-over']");
	By Arrow2 =By.xpath("//*[@id=\"ext-gen36\"]");
	By Arrow3 = By.cssSelector("div.sd_primary_tabstrip > .x-tab-tabmenu-right");
	
	
	
	
	
	public CommonElementsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, TimeoutValue), this);
		wait = new WebDriverWait(driver, TimeoutValue);
	}
	
	/**
	 * 
	 * Salesforce Logout
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created June-03-2019
	 * 
	 */
	
	public void logout(String testcaseName, String details, String elementText) throws IOException {

		clickElementByXpath(driver, testcaseName, details, "Click on the User Navigation label", UserNavLabel);
		clickLinkText(driver, testcaseName, details, "Click on Signout Link", "Logout");
	}
	
	
	/**
	 * 
	 * Salesforce Close All tabs
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @throws InterruptedException 
	 * @created June-06-2019
	 * 
	 */
	
	
	public void CloseAllTabs(String testcaseName, String details, String elementText) throws IOException, InterruptedException {
//
//		try {
//			if(!testStepStatus) {
//				return;
//			}
		
		//Thread.sleep(3000);
		moveToElementAndClick(driver, testcaseName, details, "Clicked on the Arrow", Arrow3);
		if(findElement(driver,CloseAllPrimarytabs) != null) 
		{
			clickElementByXpath(driver, testcaseName, details, "Close All Primary tabs", CloseAllPrimarytabs);
			//reportPass(driver, testcaseName, details, "Close All Primary tabs ",  "is part of the Home Page");
		} 
		else {
			
			escapeKeyPressEvent(driver, testcaseName, details,"Escape KeyPress event");
		}
//	} 
//		catch (Exception e) {
//		reportInfo(driver,testcaseName, details, "All tabs are alaready closed", "so Close All Primary tabs will not be present");
//		 testStepStatus=true;
	}
	}

		
	


