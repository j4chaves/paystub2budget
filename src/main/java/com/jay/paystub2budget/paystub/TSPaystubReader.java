package com.jay.paystub2budget.paystub;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.jay.paystub2budget.util.PaystubFieldNames;

public class TSPaystubReader implements PaystubReaderInterface {

	private File file;
	
	public TSPaystubReader(File file) {
		this.file = file;
	}
	
	@Override
	public Paystub createPaystubObject() {
		String contents = readPaystubFile(file);
		var contentsList = Arrays.asList(contents.split("\n"));
		var splitContents = new ArrayList<String>(contentsList);
		
		var paystub = new Paystub();
		var extractedFields = new HashMap<String, StubField>();
		
		// Date
		String date = splitContents.get(26).substring(9);		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate localDate = LocalDate.parse(date.trim(), formatter);
		paystub.setDate(localDate);
		
		// Regular Pay
		StubField regularPayStubField = parseField(splitContents, 32, 4, 6, PaystubFieldNames.TS_REGULAR_PAY);
		extractedFields.put(PaystubFieldNames.TS_REGULAR_PAY, regularPayStubField);
		
		// Holiday Pay
		String holidayPayRow = splitContents.get(33);
		String[] hArray = holidayPayRow.split(" ");
		if (hArray.length >= 6) {
			StubField holidayPayStubField = parseField(splitContents, 33, 4, 6, PaystubFieldNames.TS_HOLIDAY_PAY);
			extractedFields.put(PaystubFieldNames.TS_HOLIDAY_PAY, holidayPayStubField);
		}
		
		// Data Security Training Pay
		String dstPayRow = splitContents.get(34);
		String[] dstArray = dstPayRow.split(" ");
		if (dstArray.length >= 7) {
			StubField dstStubField = parseField(splitContents, 34, 5, 7, PaystubFieldNames.TS_DATA_SECURITY_TRAINING_PAY);
			extractedFields.put(PaystubFieldNames.TS_DATA_SECURITY_TRAINING_PAY, dstStubField);
		}
		
		// Total Tax Deductions
		StubField totalTaxDeductionsStubField = parseField(splitContents, 44, 3, 4, PaystubFieldNames.TS_TOTAL_TAX_DEDUCTIONS);
		extractedFields.put(PaystubFieldNames.TS_TOTAL_TAX_DEDUCTIONS, totalTaxDeductionsStubField);
		
		/**
		 * PRE-TAX DEDUCTIONS
		 */		
		// Medical
		StubField medicalStubField = parseField(splitContents, 46, 3, 4, PaystubFieldNames.TS_MEDICAL_PRE_TAX);
		extractedFields.put(PaystubFieldNames.TS_MEDICAL_PRE_TAX, medicalStubField);
		
		// Dental
		StubField dentalStubField = parseField(splitContents, 47, 3, 4, PaystubFieldNames.TS_DENTAL_PRE_TAX);
		extractedFields.put(PaystubFieldNames.TS_DENTAL_PRE_TAX, dentalStubField);
		
		// Vision
		StubField visionStubField = parseField(splitContents, 48, 2, 3, PaystubFieldNames.TS_VISION_PRE_TAX);
		extractedFields.put(PaystubFieldNames.TS_VISION_PRE_TAX, visionStubField);
		
		// Accident
		StubField accidentStubField = parseField(splitContents, 49, 3, 4, PaystubFieldNames.TS_ACCIDENT_PRE_TAX);
		extractedFields.put(PaystubFieldNames.TS_ACCIDENT_PRE_TAX, accidentStubField);
		
		// Hospital Cost Protection Plan		
		StubField hospitalStubField = parseField(splitContents, 50, 4, 5, PaystubFieldNames.TS_HOSPITAL_PRE_TAX);
		extractedFields.put(PaystubFieldNames.TS_HOSPITAL_PRE_TAX, hospitalStubField);
		
		
		paystub.setExtractedFields(extractedFields);
		
		return paystub;
	}
	
	/**
	 * Parses the desired data from the given list
	 * @param splitContents 		List to pull data from
	 * @param splitContentsIndex	Index of the data being pulled based on its position in the paystub
	 * @param currentIndex			Index of where the "current" amount is in the paystub
	 * @param ytdIndex				Index of where the "year to date" amount is in the paystub
	 * @param fieldName				Name of the field being parsed
	 */
	private StubField parseField(List<String> splitContents, int splitContentsIndex, int currentIndex, int ytdIndex, String fieldName) {
		
		String paystubRow = splitContents.get(splitContentsIndex);
		String[] rowArray = paystubRow.split(" ");
		String strippedCurrent = rowArray[currentIndex].replaceAll(",", "");
		String strippedYtd = rowArray[ytdIndex].replaceAll(",", "");
		Double current = Double.parseDouble(strippedCurrent);
		Double ytd = Double.parseDouble(strippedYtd);
		
		StubField stubField = new StubField(fieldName, current, ytd);
		return stubField;
	}
}
