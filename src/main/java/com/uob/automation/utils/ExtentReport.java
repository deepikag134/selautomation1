package com.uob.automation.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReport {

	/**
	 * Extent Reports
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-13-2019 
	 * 
	 */
	
	ExtentReports report;
	ExtentTest logger;

	PdfPTable rhighLeveSummarytable;
	PdfPTable rsummarytable;
	PdfPTable rdetailtable;

	public static void main(String[] args) throws DocumentException, IOException, ParseException {
		String reportTextFileName = System.getProperty("user.dir")+ "\\Reports\\TextReports\\SFDC_20180516_152827.txt";
		int startFileName = reportTextFileName.indexOf("TextReports");
		String ReportFileName = reportTextFileName.substring(startFileName + 12, reportTextFileName.length() - 4);
		String extentReportFileName = Constants.ExtentReportPath + ReportFileName + ".html";
		System.out.println(extentReportFileName);
		new ExtentReport().generateExtentReport(extentReportFileName, reportTextFileName);
	}

	public void generateExtentReport(String extentReportFileName, String txtFileName)
			throws DocumentException, MalformedURLException, IOException, ParseException {

		report = new ExtentReports(extentReportFileName);
		try {
			addDetailCells(txtFileName);
			report.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("EXTENT REPORT IS GENERATED");
	}

	public void addDetailCells(String fileName) throws IOException {

		String image;

		String lineData;
		String tempTestCaseName = "";
		String compareTestCase = "";
		String compareBrowser = "";
		int browserCount = 0;

		Set<String> uniqueTestCaseName = getUniqueTestCases(fileName);
		Iterator<String> iter = uniqueTestCaseName.iterator();

		while (iter.hasNext()) {
			compareTestCase = iter.next();

			Set<String> uniqueBrowserName = getUniqueBrowserDevice(fileName, compareTestCase, 1);
			Iterator<String> browserNames = uniqueBrowserName.iterator();

			while (browserNames.hasNext()) {
				logger = report.startTest(compareTestCase);
				browserCount = 0;
				compareBrowser = browserNames.next();

				FileReader inputFile = new FileReader(fileName);
				BufferedReader br = new BufferedReader(inputFile);

				while ((lineData = br.readLine()) != null) {

					String[] getValue = lineData.split("@@@@@");
					String testCaseName = getValue[0];

					if (testCaseName.equals(compareTestCase)) {

						String browserName = getValue[1];
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
								if (tempTestCaseName.equals(testCaseName)) {
								} else {
								}
								if (browserCount != 1)
									browserName = "";

								if (stepValue.equals("NO DATA"))
									stepValue = "";

								System.out
										.println(compareTestCase + "---->" + stepDescription + stepValue + screenshot);

							
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
			report.endTest(logger);
		}

	}

	public Set<String> getUniqueTestCases(String fileName) throws IOException {

		FileReader inputFile = new FileReader(fileName);
		BufferedReader br = new BufferedReader(inputFile);

		String lineData;

		Set<String> set = new HashSet<String>();

		while ((lineData = br.readLine()) != null) {

			String[] getArrayValue = lineData.split("@@@@@");
			String getValue = getArrayValue[0];

			set.add(getValue);
		}
		br.close();
		return set;
	}

	public Set<String> getUniqueBrowserDevice(String fileName, String testCaseName, int arrayIndex) throws IOException {

		FileReader inputFile = new FileReader(fileName);
		BufferedReader br = new BufferedReader(inputFile);

		String lineData;

		Set<String> set = new HashSet<String>();

		while ((lineData = br.readLine()) != null) {

			String[] getArrayValue = lineData.split("@@@@@");
			String getValue = getArrayValue[arrayIndex];

			if (testCaseName.equals(getArrayValue[0])) {
				set.add(getValue);
			}
		}
		br.close();
		return set;
	}

	public long getTimeTaken(String startDateTime, String endDateTime) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

		Date d1 = null;
		Date d2 = null;
		try {
			d1 = sdf.parse(startDateTime);
			d2 = sdf.parse(endDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long diff = d2.getTime() - d1.getTime();
		long diffSeconds = diff / 1000 % 60;
		// long diffMinutes = diff / (60 * 1000) % 60;
		return diffSeconds;
	}

	public String getCurrentDateAndTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return sdf.format(cal.getTime());
	}
}