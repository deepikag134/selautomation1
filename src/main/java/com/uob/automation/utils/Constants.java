package com.uob.automation.utils;

public class Constants {

	
	    // Max wait TIME
	    public static final int MaxWaitTimeInSec = 60;
	
		public static final String MOLURL ="http://membersonline.com";
		public static final String NWURL ="https://nw.truevalue.com/nw/nw_welcome.jsp";
		public static final String BrowserName="Firefox"; //other values are Chore and IE
		
		// IE and Chrome Driver exe files path
		public static final String FirefoxDriverPath=System.getProperty("user.dir")+"\\geckodriver.exe";
		public static final String IEDriverPath=System.getProperty("user.dir")+"\\IEDriverServer.exe";
		public static final String ChromeDriverPath=System.getProperty("user.dir") + "\\chromedriver.exe";
		
		//Constants for the test step status
		public static final String stepPass="PASS";
		public static final String stepFail="FAIL";
		public static final String stepWarn="WARN";
		public static final String stepInfo="INFO";
		

		//Timeout interval constants
		public static final int maxWaitTime=120;
		public static final int intervalWaitTime=5;
		
		public static final String DELIMITER="@@@@@";

		public static final String Logo = System.getProperty("user.dir") + "\\src\\main\\java\\com\\uob\\automation\\datafiles\\UOBLogo.png";
		
		// File locations constants
		public static final String InputFileLocation=System.getProperty("user.dir") + "\\src\\main\\java\\com\\uob\\automation\\datafiles\\InputData.xlsx";
		public static final String ExecuteFileLocation=System.getProperty("user.dir") + "\\src\\main\\java\\com\\uob\\automation\\datafiles\\ExecutionFile.xlsx";
		public static final String ExecuteFileSheet="ExecutionSheet";
		public static final String RoleColumn="userRole";
		
		
		//Test reports location details
		public static final String TextReportPath=System.getProperty("user.dir") + "\\Reports\\TextReports\\";
		public static final String HtmlReportPath=System.getProperty("user.dir") + "\\Reports\\HtmlReports\\";
		public  static final String PdfReportPath = System.getProperty("user.dir") + "\\Reports\\PdfReports\\";
		public  static final String ExtentReportPath = System.getProperty("user.dir") + "\\Reports\\ExtentReports\\";
		public static final String ScreenshotsPath=System.getProperty("user.dir") + "\\Reports\\Screenshots\\";

		
		//Screen shot preferences
	
		public static final boolean ScreenshotsToAllStepsInExtentReport=true;
		public static final boolean ScreenshotsToAllStepsInPdfReport=true;

		
		// Execution File Column Names
		public static final String PlatformName = "PlatformName";
		public static final String PlatformVersion = "PlatformVersion";
		public static final String NO_DATA = "NO DATA";
	
		public static final boolean SCREENSHOTS_TO_ALL_STEPS_IN_EXTENT_REPORT = true;
		public static final boolean SCREENSHOTS_TO_ALL_STEPS_IN_PDF_REPORT = true;
		
		
	}
