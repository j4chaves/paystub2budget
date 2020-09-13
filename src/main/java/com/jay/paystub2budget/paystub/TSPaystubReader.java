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
		String regularPayRow = splitContents.get(32);
		String[] array = regularPayRow.split(" ");
		String strippedRPay = array[4].replaceAll(",", "");
		String strippedYtdRPay = array[6].replaceAll(",", "");
		double pay = Double.parseDouble(strippedRPay);
		double ytdPay = Double.parseDouble(strippedYtdRPay);
		
		var rPayStubField = new StubField(PaystubFieldNames.TS_REGULAR_PAY, pay, ytdPay);
		extractedFields.put(PaystubFieldNames.TS_REGULAR_PAY, rPayStubField);
		
		// Holiday Pay
		String holidayPayRow = splitContents.get(33);
		String[] hArray = holidayPayRow.split(" ");
		if (hArray.length >= 6) {
			String strippedHPay = hArray[4].replaceAll(",", "");
			String strippedYtdHPay = hArray[6];
			Double hPay = Double.parseDouble(strippedHPay);
			Double ytdHPay = Double.parseDouble(strippedYtdHPay);
			
			var hPayStubField = new StubField(PaystubFieldNames.TS_HOLIDAY_PAY, pay, ytdPay);
			extractedFields.put(PaystubFieldNames.TS_HOLIDAY_PAY, hPayStubField);
		}
		
		// Data Security Training Pay
		String dstPayRow = splitContents.get(34);
		String[] dstArray = dstPayRow.split(" ");
		if (dstArray.length >= 7) {
			String strippedDSTPay = dstArray[5].replaceAll(",", "");
			String strippedYtdDSTPay = dstArray[7].replaceAll(",", "");
			Double dstPay = Double.parseDouble(strippedDSTPay);
			Double ytdDSTPay = Double.parseDouble(strippedYtdDSTPay);
			
			var dstPaystubField = new StubField(PaystubFieldNames.TS_DATA_SECURITY_TRAINING_PAY, dstPay, ytdDSTPay);
			extractedFields.put(PaystubFieldNames.TS_DATA_SECURITY_TRAINING_PAY, dstPaystubField);
		}
		
		// Total Tax Deductions
		String taxRow = splitContents.get(44);
		String[] taxArray = taxRow.split(" ");
		String strippedTaxPay = taxArray[3].replaceAll(",", "");
		String strippedYtdTax = taxArray[4].replaceAll(",", "");
		double tax = Double.parseDouble(strippedTaxPay);
		double ytdTax = Double.parseDouble(strippedYtdTax);
		
		var taxPaystubField = new StubField(PaystubFieldNames.TS_TOTAL_TAX_DEDUCTIONS, tax, ytdTax);
		extractedFields.put(PaystubFieldNames.TS_TOTAL_TAX_DEDUCTIONS, taxPaystubField);
		
		/**
		 * PRE-TAX DEDUCTIONS
		 */
		
		// Medical
		String medicalPreTaxRow = splitContents.get(46);
		String[] medicalPreTaxArray = medicalPreTaxRow.split(" ");
		String strippedMedical = medicalPreTaxArray[3].replaceAll(",", "");
		String strippedYtdMedical = medicalPreTaxArray[4].replaceAll(",", "");
		double medical = Double.parseDouble(strippedMedical);
		double ytdMedical = Double.parseDouble(strippedYtdMedical);
		
		var medicalPaystubField = new StubField(PaystubFieldNames.TS_MEDICAL_PRE_TAX, medical, ytdMedical);
		extractedFields.put(PaystubFieldNames.TS_MEDICAL_PRE_TAX, medicalPaystubField);
		
		// Dental
		String dentalPreTaxRow = splitContents.get(47);
		String[] dentalPreTaxArray = dentalPreTaxRow.split(" ");
		String strippedDental = dentalPreTaxArray[3].replaceAll(",", "");
		String strippedYtdDental = dentalPreTaxArray[4].replaceAll(",", "");
		double dental = Double.parseDouble(strippedDental);
		double ytdDental = Double.parseDouble(strippedYtdDental);
		
		var dentalPaystubField = new StubField(PaystubFieldNames.TS_DENTAL_PRE_TAX, dental, ytdDental);
		extractedFields.put(PaystubFieldNames.TS_DENTAL_PRE_TAX, dentalPaystubField);
		
		// Vision
		String visionPreTaxRow = splitContents.get(48);
		String[] visionPreTaxArray = visionPreTaxRow.split(" ");
		String strippedVision = visionPreTaxArray[2].replaceAll(",", "");
		String strippedYtdVision = visionPreTaxArray[3].replaceAll(",", "");
		double vision = Double.parseDouble(strippedVision);
		double ytdVision = Double.parseDouble(strippedYtdVision);
		
		var visionPaystubField = new StubField(PaystubFieldNames.TS_VISION_PRE_TAX, vision, ytdVision);
		extractedFields.put(PaystubFieldNames.TS_VISION_PRE_TAX, visionPaystubField);
		
		// Accident
		String accidentPreTaxRow = splitContents.get(49);
		String[] accidentPreTaxArray = accidentPreTaxRow.split(" ");
		String strippedAccident = accidentPreTaxArray[3].replaceAll(",", "");
		String strippedYtdAccident = accidentPreTaxArray[4].replaceAll(",", "");
		double accident = Double.parseDouble(strippedAccident);
		double ytdAccident = Double.parseDouble(strippedYtdAccident);
		
		var accidentPaystubField = new StubField(PaystubFieldNames.TS_ACCIDENT_PRE_TAX, accident, ytdAccident);
		extractedFields.put(PaystubFieldNames.TS_ACCIDENT_PRE_TAX, accidentPaystubField);
		
		// Hospital Cost Protection Plan
		String hospitalPreTaxRow = splitContents.get(50);
		String[] hospitalPreTaxArray = hospitalPreTaxRow.split(" ");
		String strippedHospital = hospitalPreTaxArray[4].replaceAll(",", "");
		String strippedYtdHospital = hospitalPreTaxArray[5].replaceAll(",", "");
		Double hospital = Double.parseDouble(strippedHospital);
		Double ytdHospital = Double.parseDouble(strippedYtdHospital);
		
		var hospitalPaystubField = new StubField(PaystubFieldNames.TS_HOSPITAL_PRE_TAX, hospital, ytdHospital);
		extractedFields.put(PaystubFieldNames.TS_HOSPITAL_PRE_TAX, hospitalPaystubField);
		
		
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
