//package com.uob.automation.tests;
//
//import org.apache.commons.lang3.StringUtils;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.Test;
//
//import com.uob.automation.common.BaseTest;
//import com.uob.automation.pages.CommonElementsPage;
//import com.uob.automation.pages.SalesForceLoginPage;
//import com.uob.automation.utils.Constants;
//import com.uob.automation.utils.DriverHelper;
//
//public class UOB_Login extends BaseTest {
//	String TestCaseName = "SFDC_Login";
//	String dataSheetName = "LoginData";
//	String browserName;
//	CommonElementsPage commonElementsPage;
//	SalesForceLoginPage salesforceloginpage;
//	
//
//	/**
//	 * SFDC Login Test
//	 * 
//	 * @author DeepikaGuduru
//	 * @version 1.0
//	 * @created June-5-2019
//	 * 
//	 */
//	@Test
//	public void LoginTest() {
//		String getExecutionRows = getExecutionRows(TestCaseName);
//		String[] totalExecutionRows = getExecutionRows.split(",");
//		for (int i = 0; i < totalExecutionRows.length; i++) {
//			salesforceloginpage = new SalesForceLoginPage(driver);
//			commonElementsPage = new CommonElementsPage(driver);
//			browserName = DriverHelper.getCapabilities(browser, "name");
//			try {
//				int currentRow = Integer.parseInt(totalExecutionRows[i]);
//				String platformName = getDataFromExecutionExcel(currentRow, "PlatformName");
//				String executionEnv = browserName.toUpperCase() + " - " + platformName.toUpperCase();
//				logger = report.startTest(TestCaseName + "-" + executionEnv);
//				startTest(driver, TestCaseName, executionEnv);
//				int totalInputDataRows = getRowsFromInputFile(Constants.InputFileLocation, dataSheetName);
//				for (int rowData = 2; rowData <= totalInputDataRows; rowData++) {
//					// Get the data(s) from input data files
//					String username = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "Username",
//							rowData);
//					String password = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "Password",
//							rowData);
//					String userId = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "User Account",
//							rowData);
//					if (StringUtils.isNotEmpty(username)) {
//						salesforceloginpage.loginWith(TestCaseName, executionEnv, username, password, userId);
//						commonElementsPage.CloseAllTabs(TestCaseName, executionEnv, "Close All Tabs");
//					//	commonElementsPage.logout(TestCaseName, executionEnv, "Logout");
//						
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@AfterTest
//	public void afterTest() {
//		report.endTest(logger);
//		if (driver != null) {
//			driver.quit();
//			driver = null;
//		}
//	}
//}