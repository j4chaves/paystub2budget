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
	private Date date;
	
	public PayStubReader (File file) {
		
		PDDocument doc;
		try {
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setLineSeparator("\n");
			stripper.setStartPage(1);
			stripper.setEndPage(1);
			doc = PDDocument.load(file);
			String contents = stripper.getText(doc);
			doc.close();
			
			
			List<String> contentsList = Arrays.asList(contents.split("\n"));
			ArrayList<String> splitContents = new ArrayList<String>(contentsList);
			
			for(String line : splitContents) {
				if (line.contains(TOTAL_TAXES_WITHHELD)) {
					String fieldName = line.substring(0, TOTAL_TAXES_WITHHELD.length()).trim();
					
					String subString = line.substring(TOTAL_TAXES_WITHHELD.length()).trim();
					subString = subString.replaceAll(",", "");
					String[] splitStr = subString.split(" ");
					
					double current = Double.valueOf(splitStr[0]);
					double yearToDate = Double.valueOf(splitStr[1]);
					
					extractedFields.put(fieldName, new StubField(fieldName, current, yearToDate));
				} else if (line.contains(NET_PAY)) {
					String fieldName = line.substring(0, NET_PAY.length()).trim();
					
					String subString = line.substring(NET_PAY.length()).trim();
					subString = subString.replaceAll(",", "");
					String[] splitStr = subString.split(" ");
					
					double current = Double.valueOf(splitStr[0]);
					double yearToDate = Double.valueOf(splitStr[1]);
					
					extractedFields.put(fieldName, new StubField(fieldName, current, yearToDate));
				} else if (line.contains(CHECK_DATE)) {
					String subString = line.substring(CHECK_DATE.length()).trim();
					String dateString = subString.split(" ")[0];	// This splits the string into an array and only grabs the first result of the array
					DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
					try {
						date = format.parse(dateString);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(date.toString() + "\n");
				}	else if (line.contains(TOTAL_PRETAX_DEDUCTIONS)) {
					String fieldName = line.substring(0, TOTAL_PRETAX_DEDUCTIONS.length()).trim();
					
					String subString = line.substring(TOTAL_PRETAX_DEDUCTIONS.length()).trim();
					subString = subString.replaceAll(",",  "");					
					String[] splitStr = subString.split(" ");
					
					double current = Double.valueOf(splitStr[0]);
					double yearToDate = Double.valueOf(splitStr[1]);
					
					extractedFields.put(fieldName, new StubField(fieldName, current, yearToDate));
				}
			}
			/*for(StubField stub : extractedFields) {
				System.out.println(stub.toString());
			}*/
			extractedFields.forEach((key,value) -> System.out.println(value.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public PayStub createPayStubObject() {
		PayStub stub = new PayStub();
		stub.setDate(date);
		stub.setExtractedFields(extractedFields);

		// Figure out the month
		String monthString = date.toString().substring(4, 7).toUpperCase().trim();
		Months month = Months.valueOf(monthString);
		stub.setMonth(month);
		return stub;
	}
}
