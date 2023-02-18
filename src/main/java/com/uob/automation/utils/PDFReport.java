package com.uob.automation.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFReport {
	// Set the Font properties
	final Font TITLE_FONT = new Font(FontFamily.HELVETICA, 50, Font.BOLD, BaseColor.DARK_GRAY);
	final Font FONT_14BW = new Font(FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
	final Font FONT_11BDARK_GRAY = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.DARK_GRAY);
	final Font FONT_10BBLACK = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
	final Font FONT_20BDARK_GRAY = new Font(FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.DARK_GRAY);
	final Font FONT_GREEN = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.GREEN);
	final Font FONT_RED = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.RED);
	PdfPTable rhighLeveSummarytable;
	PdfPTable rsummarytable;
	PdfPTable rdetailtable;

	/**
	 * Please do not delete this method This method is used to convert the HTML file
	 * to PDF
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-13-2019
	 * 
	 *          public static void main(String[] args) throws DocumentException,IOException, ParseException { String reportTextFileName =
	 *          "C:\\AutomationProjects\\~~~\\Reports\\TextReports\\SFDC_20170803_084932.txt";
	 *          int startFileName = reportTextFileName.indexOf("TextReports");
	 *          String ReportFileName = reportTextFileName.substring(startFileName +12, reportTextFileName.length() - 4); 
	 *          String pdfReportFileName =Constants.PdfReportPath + ReportFileName + ".pdf";
	 *          System.out.println(pdfReportFileName); new
	 *          PDFReport().generatePDFReport(pdfReportFileName,
	 *          reportTextFileName); }
	 * 
	 */
	
	
	
	
	/**
	 * PDF Report implementation
	 * 
	 * @author DeepikaGuduru
	 * @version 1.0
	 * @created April-13-2019
	 * 
	 */
	public void generatePDFReport(String pdfFileName, String txtFileName)
			throws DocumentException, MalformedURLException, IOException, ParseException {
		try {
			// Step 1 - Create the document with the size
			Document doc = new Document();
			doc.setPageSize(PageSize.A4);
			// Step 2 - Create PDFWriter instance with the created doc
			PdfWriter pdf = PdfWriter.getInstance(doc, new FileOutputStream(pdfFileName));
			// Step 3 - Open the document
			// After you use, document.newPage();, it will be ignored if you
			// don't add any content.
			// So, if you need blank page, then add writer.setPageEmpty(false);
			// right after calling newPage()
			doc.open();
			doc.newPage();
			pdf.setPageEmpty(false);
			// Add the client Image
			Image img = Image.getInstance(Constants.Logo);
			img.scaleAbsolute(200f, 200f);
			img.setAlignment(Image.MIDDLE);
			doc.add(img);
			// Add the client name
			doc.add(Chunk.NEWLINE);
			doc.add(Chunk.NEWLINE);
			Paragraph clientTitle = new Paragraph(new Phrase(getChunk("UOB Automation", TITLE_FONT)));
			clientTitle.setAlignment(Element.ALIGN_CENTER);
			doc.add(clientTitle);
			doc.add(Chunk.NEWLINE);
			// Add automation report title
			Paragraph reportTitle = new Paragraph(
					new Phrase(getChunk("Automation Execution Report", FONT_20BDARK_GRAY)));
			reportTitle.setAlignment(Element.ALIGN_CENTER);
			doc.add(reportTitle);
			// Add automation report date
			Paragraph reportdate = new Paragraph(
					new Phrase(getChunk("Date: " + getCurrentDateAndTime(), FONT_20BDARK_GRAY)));
			reportdate.setAlignment(Element.ALIGN_CENTER);
			doc.add(reportdate);
			doc.add(Chunk.NEWLINE);
			doc.add(Chunk.NEWLINE);
		
			Chunk highLevelSummaryHeader = getChunk("Automated Test Execution - High Level Summary", FONT_20BDARK_GRAY);
			highLevelSummaryHeader.setUnderline(0.1f, -2f); // 0.1 thick, -2 y-location
			Paragraph highLevelSummaryParagraph = new Paragraph();
			highLevelSummaryParagraph.setAlignment(Element.ALIGN_CENTER);
			highLevelSummaryParagraph.add(new Phrase(highLevelSummaryHeader));
			doc.add(highLevelSummaryParagraph);
			doc.add(Chunk.NEWLINE);
			float[] rhighLevelSummaryCells = { 2.1f, 2.1f, 2.1f };
			String rhighLevelSummaryTableTitle = "High Level Execution Summary";
			String[] rhighLevelSummaryheader = { "Description", "Scripts Count", "Steps Count" };
			rhighLeveSummarytable = createTable(rhighLevelSummaryCells, rhighLevelSummaryheader,
					rhighLevelSummaryTableTitle);
			addHighLevelSummaryCells1(txtFileName);
			doc.add(rhighLeveSummarytable);
			// *****************************************************************************************************************************
			// Add a new page
			doc.newPage();
			pdf.setPageEmpty(false);
			Chunk summaryHeader = getChunk("Automated Test Execution - Script Wise Summary", FONT_20BDARK_GRAY);
			summaryHeader.setUnderline(0.1f, -2f); // 0.1 thick, -2 y-location
			Paragraph summaryParagraph = new Paragraph();
			summaryParagraph.setAlignment(Element.ALIGN_CENTER);
			summaryParagraph.add(new Phrase(summaryHeader));
			doc.add(summaryParagraph);
			doc.add(Chunk.NEWLINE);
			float[] rsummaryCells = { 2.1f, 2.3f, 1.2f, 1.8f, 1.2f, 1.2f };
			String rsummaryTableTitle = "Test Scenarios Passed/Failed Summary Details";
			String[] rsummaryheader = { "Test Scenario Name", "Browser/Device", "Status", "Executed Steps",
					"Pass Steps", "Fail Steps" };
			rsummarytable = createTable(rsummaryCells, rsummaryheader, rsummaryTableTitle);
			addSummaryCells(txtFileName);
			doc.add(rsummarytable);
			doc.add(Chunk.NEWLINE);
			doc.setPageSize(PageSize.A3);
			doc.newPage();
			pdf.setPageEmpty(false);
			// *****************************************************************************************************************************
			Chunk detailHeader = getChunk("Automated Test Execution Detail Report", FONT_20BDARK_GRAY);
			detailHeader.setUnderline(0.1f, -2f); // 0.1 thick, -2 y-location
			Paragraph detailParagraph = new Paragraph();
			detailParagraph.setAlignment(Element.ALIGN_CENTER);
			detailParagraph.add(new Phrase(detailHeader));
			doc.add(detailParagraph);
			doc.add(Chunk.NEWLINE);
			detailParagraph.setAlignment(Element.ALIGN_LEFT);
			float[] rdetailCells = { 0.5f, 1.8f, 1.5f, 1.5f, 3.3f, 1.2f, 0.9f, 1.2f };
			String rdetailTableTitle = "Test Scenarios Step By Step Execution Details";
			String[] rdetailheader = { "Step No", "Test Scenario Name", "Browser/Device", "Date/Time",
					"Step Description/Activity", "Test Data / Value", "Status", "Screenshot" };
			rdetailtable = createTable(rdetailCells, rdetailheader, rdetailTableTitle);
			addDetailCells(txtFileName);
			doc.add(rdetailtable);
			Chunk endTest = getChunk("End of the Testcase", FONT_GREEN);
			detailHeader.setUnderline(0.1f, -2f);
			detailParagraph.add(new Phrase(endTest));
			doc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("PDF REPORT IS GENERATED");
	}

	public void addSummaryCells(String fileName) throws IOException, ParseException {
		int reportStepCount = 0;
		int passCount = 0;
		int failCount = 0;
		String stepStatus = "";
		String startTime = "";
		String endTime = "";
		String[] getValue = null;
		String lineData;
		String testCaseName = "";
		String compareTestCase = "";
		String compareBrowser = "";
		String browserName = "";
		Set<String> uniqueTestCaseName = getUniqueTestCases(fileName);
		Iterator<String> iter = uniqueTestCaseName.iterator();
		while (iter.hasNext()) {
			reportStepCount = 0;
			passCount = 0;
			failCount = 0;
			stepStatus = "PASS";
			compareTestCase = iter.next();
			Set<String> uniqueBrowserName = getUniqueBrowserDevice(fileName, compareTestCase, 1);
			Iterator<String> browserNames = uniqueBrowserName.iterator();
			while (browserNames.hasNext()) {
				reportStepCount = 0;
				passCount = 0;
				failCount = 0;
				compareBrowser = browserNames.next();
				FileReader inputFile = new FileReader(fileName);
				BufferedReader br = new BufferedReader(inputFile);
				while ((lineData = br.readLine()) != null) {
					getValue = lineData.split("@@@@@");
					testCaseName = getValue[0];
					if (testCaseName.equalsIgnoreCase(compareTestCase)) {
						if (reportStepCount == 0) {
							startTime = getValue[2];
						}
						browserName = getValue[1];
						if (browserName.equalsIgnoreCase(compareBrowser)) {
							reportStepCount++;
							stepStatus = getValue[5];
							// System.out.println(lineData);
							if (stepStatus.equalsIgnoreCase("PASS")) {
								passCount++;
							} else {
								failCount++;
							}
						}
					}
				}
				endTime = getValue[2];
				br.close();
				if (failCount > 0)
					stepStatus = "FAIL";
				rsummarytable.addCell(createValueCell(String.valueOf(compareTestCase)));
				rsummarytable.addCell(createValueCell(String.valueOf(compareBrowser)));
				rsummarytable.addCell(createValueCell(String.valueOf(stepStatus)));
				// rsummarytable.addCell(createValueCell(String.valueOf(reportStepCount)));
				rsummarytable.addCell(createValueCell(String.valueOf(reportStepCount)));
				rsummarytable.addCell(createValueCell(String.valueOf(passCount)));
				rsummarytable.addCell(createValueCell(String.valueOf(failCount)));
				// rsummarytable.addCell(createValueCell(String.valueOf(getTimeTaken(startTime,
				// endTime))));
			}
		}
	}

	public void addDetailCells(String fileName) throws IOException {
		int reportStepCount = 0;
		// FileReader inputFile = new FileReader(fileName);
		// BufferedReader br = new BufferedReader(inputFile);
		String lineData;
		String tempTestCaseName = "";
		String printTestCaseName = "";
		String compareTestCase = "";
		String compareBrowser = "";
		int browserCount = 0;
		String deviceCombination;
		Set<String> uniqueTestCaseName = getUniqueTestCases(fileName);
		Iterator<String> iter = uniqueTestCaseName.iterator();
		while (iter.hasNext()) {
			tempTestCaseName = "";
			compareTestCase = iter.next();
			Set<String> uniqueBrowserName = getUniqueBrowserDevice(fileName, compareTestCase, 1);
			Iterator<String> browserNames = uniqueBrowserName.iterator();
			while (browserNames.hasNext()) {
				browserCount = 0;
				compareBrowser = browserNames.next();
				deviceCombination = compareTestCase + "-" + compareBrowser;
				FileReader inputFile = new FileReader(fileName);
				BufferedReader br = new BufferedReader(inputFile);
				reportStepCount = 0;
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
								reportStepCount++;
								// /if (tempTestCaseName.equals(testCaseName)) {
								// System.out.println("************************************************");
								// System.out.println("Temp Test Case Name: " + tempTestCaseName);
								// System.out.println("Device Combination : " + deviceCombination);
								if (tempTestCaseName.equalsIgnoreCase(deviceCombination)) {
									printTestCaseName = "";
								} else {
									tempTestCaseName = deviceCombination;
									printTestCaseName = deviceCombination.substring(0, deviceCombination.indexOf("-"));
									reportStepCount = 1;
								}
								// System.out.println("Print Test Case Name: " + printTestCaseName);
								// System.out.println("************************************************");
								if (browserCount != 1)
									browserName = "";
								if (stepValue.equals("NO DATA"))
									stepValue = "";
								rdetailtable.addCell(createValueCellLeft(String.valueOf(reportStepCount)));
								rdetailtable.addCell(createValueCellLeft(String.valueOf(printTestCaseName)));
								rdetailtable.addCell(createValueCellLeft(String.valueOf(browserName)));
								rdetailtable.addCell(createValueCell(String.valueOf(dispDateTime)));
								rdetailtable.addCell(createValueCellLeft(String.valueOf(stepDescription)));
								rdetailtable.addCell(createValueCellLeft(String.valueOf(stepValue)));
								if (stepStatus.equalsIgnoreCase("PASS")) {
									rdetailtable.addCell(createPassCell(String.valueOf(stepStatus)));
								} else {
									rdetailtable.addCell(createFailCell(String.valueOf(stepStatus)));
								}
								// System.out.println(screenshot);
								Image screenshortImg = Image.getInstance(screenshot);
								screenshortImg.scalePercent(1);
								PdfPCell imgCell = new PdfPCell();
								imgCell.addElement(screenshortImg);
								if (stepStatus.equalsIgnoreCase("FAIL")) {
									rdetailtable.addCell(imgCell);
								} else {
									if (Constants.ScreenshotsToAllStepsInPdfReport) {
										rdetailtable.addCell(imgCell);
									} else {
										rdetailtable.addCell("");
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// tempTestCaseName = testCaseName;
						}
					}
				}
				br.close();
			}
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

	// create table
	public PdfPTable createTable(float[] columnsSize, String[] header, String tableTitle) throws DocumentException {
		int cellsSize = columnsSize.length;
		// create 6 column table
		PdfPTable table = new PdfPTable(cellsSize);
		// set the width of the table to 100% of page
		table.setWidthPercentage(100);
		// set relative columns width
		table.setWidths(columnsSize);
		// create header cell
		PdfPCell cell = new PdfPCell(new Phrase(tableTitle, FONT_14BW));
		// set Column span "1 cell = 6 cells width"
		cell.setColspan(cellsSize);
		// set style
		headerCellStyle(cell);
		// add to table
		table.addCell(cell);
		for (String hVal : header) {
			table.addCell(createLabelCell(hVal));
		}
		return table;
	}

	// create cells
	public PdfPCell createLabelCell(String text) {
		// create cell
		PdfPCell cell = new PdfPCell(new Phrase(text, FONT_11BDARK_GRAY));
		// set style
		labelCellStyle(cell);
		return cell;
	}

	// create cells
	public PdfPCell createValueCell(String text) {
		// create cell
		PdfPCell cell = new PdfPCell(new Phrase(text, FONT_10BBLACK));
		// set style
		valueCellStyle(cell);
		return cell;
	}

	public void headerCellStyle(PdfPCell cell) {
		// alignment
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		// padding
		cell.setPaddingTop(0f);
		cell.setPaddingBottom(7f);
		// background color
		cell.setBackgroundColor(new BaseColor(0, 121, 182));
		// border
		cell.setBorder(1);
		cell.setBorderWidthBottom(2f);
	}

	public void labelCellStyle(PdfPCell cell) {
		// alignment
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// padding
		cell.setPaddingLeft(3f);
		cell.setPaddingTop(0f);
		// background color
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// border
		// cell.setBorder(0);
		// cell.setBorderWidthBottom(1);
		cell.setBorderColorBottom(BaseColor.GRAY);
		// height
		cell.setMinimumHeight(18f);
	}

	public void valueCellStyle(PdfPCell cell) {
		// alignment
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// padding
		// cell.setPaddingTop(0f);
		// cell.setPaddingBottom(5f);
		// border
		// cell.setBorder(0);
		// cell.setBorderWidthBottom(0.5f);
		// height
		cell.setMinimumHeight(18f);
		// cell.setBorderColor(BaseColor.GREEN);
	}

	public Chunk getChunk(String txt, Font font) {
		Chunk chunk = new Chunk(txt);
		chunk.setFont(font);
		return chunk;
	}

	// create pass cells
	public PdfPCell createPassCell(String text) {
		// create cell
		PdfPCell cell = new PdfPCell(new Phrase(text, FONT_GREEN));
		// set style
		valueCellStyle(cell);
		return cell;
	}

	// create pass cells
	public PdfPCell createFailCell(String text) {
		// create cell
		PdfPCell cell = new PdfPCell(new Phrase(text, FONT_RED));
		// set style
		valueCellStyle(cell);
		return cell;
	}

	public void addHighLevelSummaryCells(String fileName) throws IOException, ParseException {
		int totalTestCaseCount = 0;
		int totalPassCount = 0;
		int totalFailCount = 0;
		int reportStepCount = 0;
		@SuppressWarnings("unused")
		int passCount = 0;
		int failCount = 0;
		String stepStatus = "";
		@SuppressWarnings("unused")
		String startTime = "";
		@SuppressWarnings("unused")
		String endTime = "";
		String[] getValue = null;
		String lineData;
		String testCaseName = "";
		String compareTestCase = "";
		String compareBrowser = "";
		String browserName = "";
		Set<String> uniqueTestCaseName = getUniqueTestCases(fileName);
		Iterator<String> iter = uniqueTestCaseName.iterator();
		totalTestCaseCount = uniqueTestCaseName.size();
		while (iter.hasNext()) {
			reportStepCount = 0;
			passCount = 0;
			failCount = 0;
			stepStatus = "PASS";
			compareTestCase = iter.next();
			Set<String> uniqueBrowserName = getUniqueBrowserDevice(fileName, compareTestCase, 1);
			Iterator<String> browserNames = uniqueBrowserName.iterator();
			while (browserNames.hasNext()) {
				compareBrowser = browserNames.next();
				FileReader inputFile = new FileReader(fileName);
				BufferedReader br = new BufferedReader(inputFile);
				while ((lineData = br.readLine()) != null) {
					getValue = lineData.split("@@@@@");
					testCaseName = getValue[0];
					if (testCaseName.equalsIgnoreCase(compareTestCase)) {
						if (reportStepCount == 0) {
							startTime = getValue[2];
						}
						browserName = getValue[1];
						if (browserName.equalsIgnoreCase(compareBrowser)) {
							reportStepCount++;
							stepStatus = getValue[5];
							// System.out.println(lineData);
							if (stepStatus.equalsIgnoreCase("PASS")) {
								passCount++;
							} else {
								failCount++;
							}
						}
					}
				}
				endTime = getValue[2];
				br.close();
				if (failCount > 0) {
					stepStatus = "FAIL";
					totalFailCount++;
				} else {
					totalPassCount++;
				}
			}
		}
		rhighLeveSummarytable.addCell(createValueCell(String.valueOf(totalTestCaseCount)));
		rhighLeveSummarytable.addCell(createValueCell(String.valueOf(totalPassCount)));
		rhighLeveSummarytable.addCell(createValueCell(String.valueOf(totalFailCount)));
	}

	public void addHighLevelSummaryCells1(String fileName) throws IOException, ParseException {
		int lineNumber = 0;
		@SuppressWarnings("unused")
		int totalTestCaseCount = 0;
		int totalPassCount = 0;
		int totalPassStepCount = 0;
		int totalFailStepCount = 0;
		int totalFailCount = 0;
		int reportStepCount = 0;
		@SuppressWarnings("unused")
		int passCount = 0;
		int failCount = 0;
		String stepStatus = "";
		String[] getValue = null;
		String lineData;
		String testCaseName = "";
		String compareTestCase = "";
		String compareBrowser = "";
		String browserName = "";
		Set<String> uniqueTestCaseName = getUniqueTestCases(fileName);
		Iterator<String> iter = uniqueTestCaseName.iterator();
		totalTestCaseCount = uniqueTestCaseName.size();
		try {
			while (iter.hasNext()) {
				reportStepCount = 0;
				passCount = 0;
				failCount = 0;
				stepStatus = "PASS";
				compareTestCase = iter.next();
				Set<String> uniqueBrowserName = getUniqueBrowserDevice(fileName, compareTestCase, 1);
				Iterator<String> browserNames = uniqueBrowserName.iterator();
				while (browserNames.hasNext()) {
					compareBrowser = browserNames.next();
					FileReader inputFile = new FileReader(fileName);
					BufferedReader br = new BufferedReader(inputFile);
					while ((lineData = br.readLine()) != null) {
						System.out.println("Current Line Number : " + lineNumber++);
						getValue = lineData.split("@@@@@");
						testCaseName = getValue[0];
						if (testCaseName.equalsIgnoreCase(compareTestCase)) {
							if (reportStepCount == 0) {
							}
							browserName = getValue[1];
							if (browserName.equalsIgnoreCase(compareBrowser)) {
								reportStepCount++;
								stepStatus = getValue[5];
								if (stepStatus.equalsIgnoreCase("PASS")) {
									passCount++;
								} else {
									failCount++;
								}
							}
						}
					}
					br.close();
					if (failCount > 0) {
						stepStatus = "FAIL";
						totalFailCount++;
					} else {
						totalPassCount++;
					}
				}
			}
			FileReader inputFile1 = new FileReader(fileName);
			@SuppressWarnings("resource")
			BufferedReader br1 = new BufferedReader(inputFile1);
			while ((lineData = br1.readLine()) != null) {
				if (lineData.contains("PASS")) {
					totalPassStepCount++;
				} else {
					totalFailStepCount++;
				}
			}
			rhighLeveSummarytable.addCell(createValueCell(String.valueOf("TOTAL")));
			rhighLeveSummarytable.addCell(createValueCell(String.valueOf(totalPassCount + totalFailCount)));
			rhighLeveSummarytable.addCell(createValueCell(String.valueOf((totalPassStepCount + totalFailStepCount))));
			rhighLeveSummarytable.addCell(createValueCell(String.valueOf("PASS")));
			rhighLeveSummarytable.addCell(createValueCell(String.valueOf(totalPassCount)));
			rhighLeveSummarytable.addCell(createValueCell(String.valueOf(totalPassStepCount)));
			rhighLeveSummarytable.addCell(createValueCell(String.valueOf("FAIL")));
			rhighLeveSummarytable.addCell(createValueCell(String.valueOf(totalFailCount)));
			rhighLeveSummarytable.addCell(createValueCell(String.valueOf(totalFailStepCount)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// create cells
	public PdfPCell createValueCellLeft(String text) {
		// create cell
		PdfPCell cell = new PdfPCell(new Phrase(text, FONT_10BBLACK));
		// set style
		valueCellStyle(cell);
		return cell;
	}

	public void valueCellStyleLeft(PdfPCell cell) {
		// alignment
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// padding
		cell.setMinimumHeight(18f);
	}
}