package com.uob.automation.tests;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.uob.automation.common.BaseTest;
import com.uob.automation.pages.CommonElementsPage;
import com.uob.automation.pages.SalesForceLoginPage;
import com.uob.automation.utils.Constants;
import com.uob.automation.utils.DriverHelper;

public class UOB_LoginmultiTest extends BaseTest {
	String TCName = "";
	int dataRow = 0;
	String userRole = "";
	String tempRole = "";
	String username = null;
	String password = null;
	String userId = "";
	String executionEnv = "";
	boolean referInputData = false;
	String getExecutionRows = null;
	String deviceName = "";
	String deviceModel = "";
	String details = "";
	String platformName = "";
	String platformVersion = "";
	String executionRows = "";
	boolean launchAndLogin = false;
	String[] totalExecutionRows = null;
	String TestCaseName = "SFDC_LoginmultiTest";
	String dataSheetName = "sfdclogindata";
	String browserName;
	CommonElementsPage commonElementsPage;
	SalesForceLoginPage salesforceloginpage;
	String Description = "";
	String platformDetails = "";
	String deviceDetails = "";

	@Test
	public void LoginTest() throws Exception {
		System.out.println(TestCaseName);
		String getExecutionRows = getExecutionRows(TestCaseName);
		String dataSheetName = "sfdclogindata";
		System.out.println("Total Execution Rows : " + getExecutionRows);
		String[] totalExecutionRows = getExecutionRows.split(",");
		for (int i = 0; i < totalExecutionRows.length; i++) {
			salesforceloginpage = new SalesForceLoginPage(driver);
			commonElementsPage = new CommonElementsPage(driver);
			browserName = DriverHelper.getCapabilities(browser, "name");
			try {
				int currentRow = Integer.parseInt(totalExecutionRows[i]);
				currentRow = Integer.parseInt(totalExecutionRows[i]);
				platformName = getDataFromExecutionExcel(currentRow, "PlatformName");
				executionRows = getDataFromExecutionExcel(currentRow, "ExecutionRows");
				username = getDataFromExecutionExcel(currentRow, "Username");
				password = getDataFromExecutionExcel(currentRow, "Password");
				String platformName = getDataFromExecutionExcel(currentRow, "PlatformName");
				String browserName = getDataFromExecutionExcel(currentRow, "BrowserName");
			//	executionEnv = browserName.toUpperCase() + " - " + platformName.toUpperCase();
				deviceDetails = browserName.toUpperCase() + " - " + platformName.toUpperCase();
				// logger = report.startTest(TestCaseName + "-" + executionEnv);
				// startTest(driver, TestCaseName, executionEnv);
				// int totalInputDataRows = getRowsFromInputFile(Constants.InputFileLocation,
				// dataSheetName);
				if (username.contains("Data") || password.equals("Data")) {
					referInputData = true;
				}
				String[] returnRows = returnExecutionRows(executionRows);
				for (int rowData = 0; rowData < returnRows.length; rowData++) {
					testStepStatus = true;
					// Get the data(s) from input data files
					dataRow = Integer.parseInt(returnRows[rowData]) + 1;
					TCName = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "TCName", dataRow);
					userRole = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "userRole", dataRow);
					Description = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "Description",
							dataRow);
					if (referInputData) {
						username = getDataFromInputFileLoginSheet(Constants.InputFileLocation, dataSheetName,
								Constants.RoleColumn, dataRow, "Username");
						password = getDataFromInputFileLoginSheet(Constants.InputFileLocation, dataSheetName,
								Constants.RoleColumn, dataRow, "Password");
						if (username == null || password == null) {
							username = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "Username",
									dataRow);
							password = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "Password",
									dataRow);
						}
					}
					if (!tempRole.equalsIgnoreCase(userRole) || FailStepCount > 0) {
						launchAndLogin = true;
					}
					switch (TCName.toUpperCase()) {
					case "TC001":
					TestCaseName = this.getClass().getSimpleName() + "_" + TCName + "_" + Description + "_" + userRole;
					TC001();
					break;
						
					case "TC002":
					TestCaseName = this.getClass().getSimpleName() + "_" + TCName + "_" + Description +"_" + userRole;
					TC002();
					break;

					}
					tempRole = userId;
					launchAndLogin = false;
					System.out.println(
							"*************************************************************************************************");
					System.out.println(
							"*************************************************************************************************");
				}
			} finally {
				report.endTest(logger);
			}
		}
	}

	public void TC001() throws Exception {
		logger = report.startTest(TestCaseName + "-" + deviceDetails);
		reportTestCase(driver, TestCaseName, deviceDetails, "PM0-13456","TC001_login Scenario");
		salesforceloginpage.loginWith(TestCaseName, deviceDetails, username, password, userId);
		//commonElementsPage.logout(TestCaseName, deviceDetails, "Logout");
	}

	public void TC002() throws Exception {
		//logger = report.startTest(TestCaseName + "-" + deviceDetails);
		reportTestCase(driver, TestCaseName, deviceDetails, "PM0-13456","TC002_login Scenario");
		salesforceloginpage.loginWith(TestCaseName, executionEnv, username, password, userId);
		//driver.close();
        //commonElementsPage.logout(TestCaseName, executionEnv, "Logout");
		
	}
	@AfterTest
	public void afterTest() {
		report.endTest(logger);
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}
}
