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
import com.jay.paystub2budget.util.PaystubFieldNames;


public class GDPaystubReader implements PaystubReaderInterface{	
	
	private Map<String, StubField> extractedFields = new HashMap<String, StubField>();
	private Date paystubDate;
	private File file;
	
	public GDPaystubReader (File file) {
		this.file = file;
	}
	
	@Override
	public Paystub createPaystubObject() {
		parseData();
		
		Paystub stub = new Paystub();
		stub.setDate(paystubDate);
		stub.setExtractedFields(extractedFields);

		// Figure out the month
		String monthString = paystubDate.toString().substring(4, 7).toUpperCase().trim();
		Months month = Months.valueOf(monthString);
		stub.setMonth(month);
		return stub;
	}
	
	private void parseData() {

		String contents = readPaystubFile(this.file);
		
		List<String> contentsList = Arrays.asList(contents.split("\n"));
		ArrayList<String> splitContents = new ArrayList<String>(contentsList);

		for(String line : splitContents) {
			if (line.contains(PaystubFieldNames.GD_TOTAL_TAXES_WITHHELD)) {
				String[] splitStr = splitField(line, PaystubFieldNames.GD_TOTAL_TAXES_WITHHELD);

				double current = Double.valueOf(splitStr[0]);
				double yearToDate = Double.valueOf(splitStr[1]);

				String fieldName = line.substring(0, PaystubFieldNames.GD_TOTAL_TAXES_WITHHELD.length()).trim();
				extractedFields.put(fieldName, new StubField(fieldName, current, yearToDate));
			} else if (line.contains(PaystubFieldNames.GD_NET_PAY)) {
				String[] splitStr = splitField(line, PaystubFieldNames.GD_NET_PAY);

				double current = Double.valueOf(splitStr[0]);
				double yearToDate = Double.valueOf(splitStr[1]);

				String fieldName = line.substring(0, PaystubFieldNames.GD_NET_PAY.length()).trim();
				extractedFields.put(fieldName, new StubField(fieldName, current, yearToDate));
			}	else if (line.contains(PaystubFieldNames.GD_TOTAL_PRETAX_DEDUCTIONS)) {
				String[] splitStr = splitField(line, PaystubFieldNames.GD_TOTAL_PRETAX_DEDUCTIONS);

				double current = Double.valueOf(splitStr[0]);
				double yearToDate = Double.valueOf(splitStr[1]);

				String fieldName = line.substring(0, PaystubFieldNames.GD_TOTAL_PRETAX_DEDUCTIONS.length()).trim();
				extractedFields.put(fieldName, new StubField(fieldName, current, yearToDate));
			}  else if (line.contains(PaystubFieldNames.GD_CHECK_DATE)) {
				String subString = line.substring(PaystubFieldNames.GD_CHECK_DATE.length()).trim();
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
	
	private String[] splitField(String line, String field) {		
		String subString = line.substring(field.length()).trim();
		subString = subString.replaceAll(",", "");
		String[] splitStr = subString.split(" ");
		
		return splitStr;
	}
}
