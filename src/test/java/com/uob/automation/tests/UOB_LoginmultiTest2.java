//
//package com.uob.automation.tests;
//
//
//
//import org.openqa.selenium.WebDriver;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.Test;
//
//import com.uob.automation.common.BaseTest;
//import com.uob.automation.pages.CommonElementsPage;
//import com.uob.automation.pages.SalesForceLoginPage;
//import com.uob.automation.utils.Constants;
//import com.uob.automation.utils.DriverHelper;
//
//	public class UOB_LoginmultiTest2 extends BaseTest {
//		
//		String TestCaseName = this.getClass().getSimpleName();
//		String dataSheetName = "sfdclogindata";
//        WebDriver driver;
//
//		boolean TestCaseFound = false;
//		String getExecutionRows = null;
//		String[] totalExecutionRows = null;
//		int currentRow = 0;
//		int dataRow=0;
//		
//		String userName = null;
//		String Password = null;
//		
//		String deviceName = "";
//		String deviceModel = "";
//		String platformName = "";
//		String platformVersion = "";
//		String appPackage = "";
//		String appActivity = "";
//		String deviceDetails = "";
//		String executionRows="";
//		String Description="";
//		String executionEnv ="";
//
//		int totalInputDataRows = 0;
//
//		String username = null;
//		String password = null;
//		boolean referInputData=false;
//		String TCName="";
//		boolean newUser = false;
//		String userRole="";
//		String tempRole="";
//		int testStartCount=0;
//		String udid=null;
//		String bundleId=null;
//		boolean launchAndLogin=false;
//		
//
//		String userId ="";
//		String AccountNumber="";
//		String EventType="";
//		String AccountName="";
//		String Address1="";
//		String StartEndTime="";
//		String EventDescription="";
//		String RelatedObject="";
//
//		String CustomerNumber="";
//		String CustomerName="";
//		String ProductNumber="";
//		String ProductNumber1="";
//		String Quantity="";
//		String browserName;
//		String PONumber="";
//		
//		CommonElementsPage commonElementsPage;
//		SalesForceLoginPage salesforceloginpage;
//
//		@Test
//		public void sfdcLogin() throws Exception {
//
//			// Get the total number of rows from the execution file
//					// for the specified test script name. The sting will be returned with the rows
//					// Ex: 2,4
//					getExecutionRows = getExecutionRows(TestCaseName);
//					String dataSheetName = "sfdclogindata";
//					
//					System.out.println("Total Execution Rows : " + getExecutionRows);
//
//					// Split the rows and put it in an array
//					totalExecutionRows = getExecutionRows.split(",");
//
//					// Loop through the number of times the script has to be executed
//					// in different devices
//					for (int iteration = 0; iteration < totalExecutionRows.length; iteration++) {
//						
////						salesforceloginpage = new SalesForceLoginPage(driver);
////						commonElementsPage = new CommonElementsPage(driver);
//						//browserName = DriverHelper.getCapabilities(browser, "name");
//						try {
//							// Get the device details from the execution file
//						//	int currentRow = Integer.parseInt(totalExecutionRows[iteration]);
//							currentRow = Integer.parseInt(totalExecutionRows[iteration]);
//						
//							platformName = getDataFromExecutionExcel(currentRow, "PlatformName");
//							
//							executionRows =  getDataFromExecutionExcel(currentRow, "ExecutionRows");
//							
//							
//							username = getDataFromExecutionExcel(currentRow, "Username");
//							password = getDataFromExecutionExcel(currentRow, "Password");
//							
////							String platformName = getDataFromExecutionExcel(currentRow, "PlatformName");
////							String executionEnv = browserName.toUpperCase() + " - " + platformName.toUpperCase();
////							logger = report.startTest(TestCaseName + "-" + executionEnv);
////							startTest(driver, TestCaseName, executionEnv);
////							
////							System.out.println("Username and Password: " + username + " " + password);
//							
////							udid =  getDataFromExecutionExcel(currentRow, "Udid");
////							bundleId = getDataFromExecutionExcel(currentRow, "Bundleid");
//							// Get the details of the device, this is to be used to reporting purpose
//							//deviceDetails = deviceModel + "-" + platformName + "-" + platformVersion;
//							
//							if(username.contains("Data") || password.equals("Data")) {
//								referInputData=true;
//							}
//							//referInputData=true;
//							// Get the execution rows from the execution file. The scripts will be executed for the row numbers specified in the file.
//							//totalInputDataRows = getRowsFromInputFile(Constants.InputFileLocation, dataSheetName);
//							String[] returnRows = returnExecutionRows(executionRows);
//							// Loop through the number of times the script has to be executed with different data
//							for (int execLoop = 0; execLoop < returnRows.length; execLoop++) {
//								//SalesToolLogin = new LoginPage(driver);
//								
//								testStepStatus = true;
//								
//								dataRow = Integer.parseInt(returnRows[execLoop])+1;
//								TCName = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "TCName", dataRow);
//								userRole = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "userRole", dataRow);
//								Description = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "Description", dataRow);
//								
//								if(referInputData) {
//									userName = getDataFromInputFileLoginSheet(Constants.InputFileLocation, dataSheetName,Constants.RoleColumn, dataRow,"Username");
//									Password = getDataFromInputFileLoginSheet(Constants.InputFileLocation, dataSheetName,Constants.RoleColumn, dataRow,"Password");
//									
//									if(userName == null || Password ==null) {
//										userName = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "Username", dataRow);
//										Password = getDataFromInputFile(Constants.InputFileLocation, dataSheetName, "Password", dataRow);
//									}
//								}
//								
//								if(!tempRole.equalsIgnoreCase(userRole) || FailStepCount > 0) {
//									//launchApp();
//									launchAndLogin=true;
//								}
//								
//								switch (TCName.toUpperCase()) {
//								case "TC001":
//									TestCaseName = this.getClass().getSimpleName() + "_" + TCName + "_" + Description +"_" + userRole;
//									TC001();
//									break;
//								case "TC002":
//									TestCaseName = this.getClass().getSimpleName() + "_" + TCName + "_" + Description +"_" + userRole;
//									TC002();
//									break;
//							
//							}
//							tempRole=userRole;
//							launchAndLogin=false;
//					}
//					System.out.println(
//							"*************************************************************************************************");
//					System.out.println(
//							"*************************************************************************************************");
//				} finally {
//					report.endTest(logger);
//				//	destroyDriver();
//				}
//			}
//			}
//											
//								
////		public void launchApp() throws Exception {
////			// Create and assign the mobile driver
////			if (driver!=null) {
////				driver.quit();
////			}
////			createMobileDriver(deviceModel, deviceName, platformVersion);
////			this.driver = getWebDriver();
////		}
//								
//	//TC001_SalesTool_Validate Customer has submit the Order thru search page
//	public void TC001() throws Exception {
//		logger = report.startTest(TestCaseName);
//		reportTestCase(driver, TestCaseName, deviceDetails, "TC001_login Scenario","PM0-13456");
//		salesforceloginpage.loginWith(TestCaseName, executionEnv, username, password, userRole);
//		commonElementsPage.logout(TestCaseName, executionEnv, "Logout");
//	}
//
//
//
//	//TC002_SalesTool_Validate Customer has submit the Order thru Fly Menu -->search --> Shopping List
//	public void TC002() throws Exception {
//		logger = report.startTest(TestCaseName);
//		reportTestCase(driver, TestCaseName, deviceDetails, "TC001_login Scenario","PM0-13456");
//		salesforceloginpage.loginWith(TestCaseName, executionEnv, username, password, userRole);
//		commonElementsPage.logout(TestCaseName, executionEnv, "Logout");
//		
//		
//	}
//
//
//
//	@AfterTest
//	public void afterTest() {
//		report.endTest(logger);
//		if (driver != null) {
//			driver.quit();
//			driver = null;
//		}
//
//
//	
//	}
//	
//	
//}
