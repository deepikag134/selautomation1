package com.uob.automation.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.io.Files;



public class HtmlReport {
	
	/**
	 * HTML Report
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created May-27-2019 
	 * 
	 */
	
	
	public File htmlReportFile;
	public String strFileName;
	public String strTestCaseName;
//	public boolean includeScreenshots=Constants.includeScreenshots;
	public String screenshotsPath=Constants.ScreenshotsPath;
	public PrintStream printhtml;
	
	public static void main (String[] args) throws IOException{
		new HtmlReport().createHtmlFile("C:/MavenProject/Reports/HtmlReports/MOL Tests_20170430_160632.html","C:/MavenProject/Reports/HtmlReports/Text/MOL Tests_20170430_160631.txt");
	}
	
	public void createHtmlFile(String htmlFileName, String txtFileName) throws IOException{
		htmlReportFile = new File(htmlFileName);
			try {
				Files.createParentDirs(htmlReportFile);
				createHtmlFileHeader(txtFileName);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	public void createHtmlFileHeader(String txtFileName) throws IOException{
		System.out.println("Inside...." + Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			String exeDateTime = sdf.format(cal.getTime());
						
			OutputStream htmlfile= new FileOutputStream(htmlReportFile);
			printhtml = new PrintStream(htmlfile);
			
			printhtml.println("<html>");
			printhtml.println("<title>Test ResulHtmlFile</title>");
			printhtml.println("<head></head>");
			printhtml.println("<body>");
			printhtml.println("<font face='Tahoma' size='3'>");
//			printhtml.println("<h2><font color='Blue' face='Tahoma' size='3'>Test Case Name : " + this.strTestCaseName +"</font></h2>");
			printhtml.println("<b>Host :</b> " + InetAddress.getLocalHost().getHostName());
			printhtml.println("<br><b>Exec Date/Start Time :</b>" + exeDateTime + "</br>");  
			printhtml.println("<b>Executed By : </b>" + System.getProperty("user.name"));
			printhtml.println("</font>");
			printhtml.println("<table border='1' width='100%' height='47'>");
			printhtml.println("<tr>");
			printhtml.println("<td width='5%' bgcolor='#CCCCFF' align='center'><b><font color='Black' face='Tahoma' size='2'>Step No.</font></b></td>");
			printhtml.println("<td width='5%' bgcolor='#CCCCFF' align='center'><b><font color='Black' face='Tahoma' size='2'>Test case Name</font></b></td>");
			printhtml.println("<td width='10%' bgcolor='#CCCCFF' align='center'><b><font color='Black' face='Tahoma' size='2'>Browser</font></b></td>");
			printhtml.println("<td width='3%' bgcolor='#CCCCFF' align='center'><b><font color='Black' face='Tahoma' size='2'>Date/Time</font></b></td>");
			printhtml.println("<td width='30%' bgcolor='#CCCCFF' align='center'><b><font color='Black' face='Tahoma' size='2'>Step Description / Activity</font></b></td>");
			printhtml.println("<td width='10%' bgcolor='#CCCCFF' align='center'><b><font color='Black' face='Tahoma' size='2'>Test Data/Value</font></b></td>");
			printhtml.println("<td width='10%' bgcolor='#CCCCFF' align='center'><b><font color='Black' face='Tahoma' size='2'>Status</font></b></td>");
			printhtml.println("<td width='10%' bgcolor='#CCCCFF' align='center'><b><font color='Black' face='Tahoma' size='2'>Screenshot</font></b></td>");
			printhtml.println("</tr>");
			//printhtml.close();
			//System.out.println("Complete");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addReportDetails(txtFileName);
		
	}
	
	public void addReportDetails(String fileName) throws IOException {

		int reportStepCount = 0;

		String lineData;
		String tempTestCaseName = "";
		String printTestCaseName = "";
		String compareTestCase="";
		String compareBrowser="";
		int browserCount=0;

		Set<String> uniqueTestCaseName = uniqueValues(fileName, "", 0);
		Iterator<String> iter = uniqueTestCaseName.iterator();

		while (iter.hasNext()) {
			compareTestCase = iter.next();

			Set<String> uniqueBrowserName = uniqueValues(fileName, compareTestCase, 1);
			Iterator<String> browserNames = uniqueBrowserName.iterator();

			while (browserNames.hasNext()) {

				browserCount=0;
				compareBrowser = browserNames.next();
//				System.out.println("compareBrowser : " + compareBrowser);
				
				FileReader inputFile = new FileReader(fileName);
				BufferedReader br = new BufferedReader(inputFile);

				while ((lineData = br.readLine()) != null) {

					String[] getValue = lineData.split("@@@@@");
					String testCaseName = getValue[0];

					if (testCaseName.equals(compareTestCase)) {

						String browserName = getValue[1];
//						System.out.println(browserName.equals(compareBrowser));
						if (browserName.equals(compareBrowser)) {
							browserCount++;
							
							String DateTime = getValue[2];
							String stepDescription = getValue[3];
							String stepValue = getValue[4];
							String stepStatus = getValue[5];
							String screenshot = getValue[6];

							String year = DateTime.substring(0, 4);
							String month = DateTime.substring(4, 6);
							String date = DateTime.substring(6, 8);

							String hour = DateTime.substring(9, 11);
							String min = DateTime.substring(11, 13);
							String sec = DateTime.substring(13, 15);

							String dispDateTime = month + "/" + date + "/" + year + " - " + hour + ":" + min + ":"
									+ sec;

							try {
								// start a new row in the html table
								reportStepCount++;
								if (tempTestCaseName.equals(testCaseName)) {
									printTestCaseName = "";
								} else {
									printTestCaseName = testCaseName;
									reportStepCount = 1;
								}
								if(browserCount!=1)browserName="";

								if (stepValue.equals("NO DATA"))
									stepValue = "";
								
								UpdateHtmlTable(reportStepCount, printTestCaseName, browserName, dispDateTime, stepDescription, stepValue,  stepStatus,screenshot);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							tempTestCaseName = testCaseName;
						}
					}
				}
				br.close();
			}
		}
		closeReport();
	}
	public void closeReport(){
		printhtml.println("</table>");
		printhtml.println("</body>");
		printhtml.println("</html>");
		printhtml.close();
	}
	
	public void UpdateHtmlTable(int reportStepCount, String printTestCaseName, String browserName, String dispDateTime, String stepDescription, String stepValue,  String stepStatus, String screenshot) throws MalformedURLException{
		
		printhtml.println("<tr>");
		printhtml.println(
				"<td width='5%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font face='Calibri' size='2'>" + reportStepCount + "</font></b></td>");
		printhtml.println(
				"<td width='15%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font face='Calibri' size='2'>" + printTestCaseName + "</font></b></td>");
		printhtml.println(
				"<td width='20%' bgcolor='#FFFFDC' valign='middle' align='left'><b><font face='Calibri' size='2'>" 	+ browserName + "</font></b></td>");
		printhtml.println(
				"<td width='10%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font face='Calibri' size='2'>" + dispDateTime + "</font></b></td>");
		
		if(stepDescription.contains("APPLITOOLS")){
			printhtml.println(
				"<td width='30%' bgcolor='#FFFFDC' valign='top' align='justify'><b><font color='Blue' face='Calibri' size='2'>" + stepDescription + "</font></b></td>");
		} else {
			printhtml.println(
					"<td width='30%' bgcolor='#FFFFDC' valign='top' align='justify'><b><font face='Calibri' size='2'>" + stepDescription + "</font></b></td>");
		}
		printhtml.println(
				"<td width='10%' bgcolor='#FFFFDC' valign='top' align='justify'><b><font face='Calibri' size='2'>" + stepValue + "</font></b></td>");

		if (stepStatus.equalsIgnoreCase("PASS")) {
			printhtml.println(
					"<td width='10%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Green'  face='Calibri' size='2'>"
							+ stepStatus + "</font></b></td>");
		} else if (stepStatus.equalsIgnoreCase("WARN")) {
			printhtml.println(
					"<td width='10%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Blue'  face='Calibri' size='2'>"
							+ stepStatus + "</font></b></td>");
		} else if (stepStatus.equalsIgnoreCase("INFO")) {
			printhtml.println(
					"<td width='10%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Orange'  face='Calibri' size='2'>"
							+ stepStatus + "</font></b></td>");
		} else {
			printhtml.println(
					"<td width='10%' bgcolor='#FFFFDC' valign='middle' align='center'><b><font color='Red' face='Calibri' size='3'>"
							+ stepStatus + "</font></b></td>");
		}
		
		String screenshot1= screenshot.substring(screenshot.indexOf("Screenshots")+12);
//		System.out.println("Screen shot : " +screenshot1 );
		printhtml.println("<td width='10%'><a href=\""+new File(screenshot).toURI().toURL()+"\">" + screenshot1 + "</a></td>");

		printhtml.println("</tr>");// end the row in the html table

		
	}
	public Set<String> uniqueValues(String fileName, String testCaseName, int arrayIndex) throws IOException {

		FileReader inputFile = new FileReader(fileName);
		BufferedReader br = new BufferedReader(inputFile);

		String lineData;
		
		Set <String> set = new HashSet<String>();

		while ((lineData = br.readLine()) != null) {

			String[] getArrayValue = lineData.split("@@@@@");
			String getValue = getArrayValue[arrayIndex];

			if (testCaseName.equals("")) {
					set.add(getValue);
			} 
			
			if (testCaseName.equals(getArrayValue[0])) {
				set.add(getValue);
			}
		}
		br.close();
		return set;
	}
}

	