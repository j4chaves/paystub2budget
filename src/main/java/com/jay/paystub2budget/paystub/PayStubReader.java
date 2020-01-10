package com.jay.paystub2budget.paystub;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.jay.paystub2budget.util.Months;


public class PayStubReader {
	
	
	public static final String NET_PAY = "Net Pay";
	public static final String TOTAL_PRETAX_DEDUCTIONS = "Total Pre-tax Deductions";
	public static final String TOTAL_TAXES_WITHHELD = "Total Taxes Withheld";
	public static final String CHECK_DATE = "Check Date:";
	
	private Map<String, StubField> extractedFields = new HashMap<String, StubField>();
	private Date paystubDate;		// TODO should be changed to paystubDate
	private File file;
	
	public PayStubReader (File file) {
		this.file = file;
	}
	
	public PayStub createPayStubObject() {
		parseData();
		
		PayStub stub = new PayStub();
		stub.setDate(paystubDate);
		stub.setExtractedFields(extractedFields);

		// Figure out the month
		String monthString = paystubDate.toString().substring(4, 7).toUpperCase().trim();
		Months month = Months.valueOf(monthString);
		stub.setMonth(month);
		return stub;
	}
	
	private void parseData() {

		String contents = readFile();
		
		List<String> contentsList = Arrays.asList(contents.split("\n"));
		ArrayList<String> splitContents = new ArrayList<String>(contentsList);

		for(String line : splitContents) {
			if (line.contains(TOTAL_TAXES_WITHHELD)) {
				String[] splitStr = splitField(line, TOTAL_TAXES_WITHHELD);

				double current = Double.valueOf(splitStr[0]);
				double yearToDate = Double.valueOf(splitStr[1]);

				String fieldName = line.substring(0, TOTAL_TAXES_WITHHELD.length()).trim();
				extractedFields.put(fieldName, new StubField(fieldName, current, yearToDate));
			} else if (line.contains(NET_PAY)) {
				String[] splitStr = splitField(line, NET_PAY);

				double current = Double.valueOf(splitStr[0]);
				double yearToDate = Double.valueOf(splitStr[1]);

				String fieldName = line.substring(0, NET_PAY.length()).trim();
				extractedFields.put(fieldName, new StubField(fieldName, current, yearToDate));
			}	else if (line.contains(TOTAL_PRETAX_DEDUCTIONS)) {
				String[] splitStr = splitField(line, TOTAL_PRETAX_DEDUCTIONS);

				double current = Double.valueOf(splitStr[0]);
				double yearToDate = Double.valueOf(splitStr[1]);

				String fieldName = line.substring(0, TOTAL_PRETAX_DEDUCTIONS.length()).trim();
				extractedFields.put(fieldName, new StubField(fieldName, current, yearToDate));
			}  else if (line.contains(CHECK_DATE)) {
				String subString = line.substring(CHECK_DATE.length()).trim();
				String dateString = subString.split(" ")[0];
				DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
				try {
					paystubDate = format.parse(dateString);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		extractedFields.forEach((key,value) -> System.out.println(value.toString()));
	}

	public String readFile() {
		
		String contents = "";

		if (this.file != null || !this.file.exists()) {
			//contents = "";	// TODO Handle better
		}
		
		PDDocument doc;
		
		try {
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setLineSeparator("\n");
			stripper.setStartPage(1);
			stripper.setEndPage(1);
			doc = PDDocument.load(file);
			contents = stripper.getText(doc);
			doc.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return contents;
	}
	
	private String[] splitField(String line, String field) {		
		String subString = line.substring(field.length()).trim();
		subString = subString.replaceAll(",", "");
		String[] splitStr = subString.split(" ");
		
		return splitStr;
	}
}
