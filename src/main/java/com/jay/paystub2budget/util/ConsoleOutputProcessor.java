package com.jay.paystub2budget.util;

import java.util.Map;

import com.jay.paystub2budget.paystub.Paystub;
import com.jay.paystub2budget.paystub.StubField;

public final class ConsoleOutputProcessor {
	
	private Map<String, StubField> fields;
	private Paystub stub;
	
	public ConsoleOutputProcessor(Paystub stub) {
		this.stub = stub;
		this.fields = stub.getExtractedFields();
	}

	public void printOutput() {		
		
		System.out.println("----- Paystub Analyzer Results -----");
		System.out.println("Paystub Date: " + stub.getDate());	//TODO This is printing the current data, not the paystub date
		
		printFieldAndAmount(PaystubFieldNames.TS_REGULAR_PAY);		
		
		printFieldAndAmount(PaystubFieldNames.TS_HOLIDAY_PAY);
		
		printFieldAndAmount(PaystubFieldNames.TS_DATA_SECURITY_TRAINING_PAY);
		
		printFieldAndAmount(PaystubFieldNames.TS_TOTAL_TAX_DEDUCTIONS);
		
		System.out.print("\n");
		System.out.println("**Pre-Tax Dedcuctions**");
		printFieldAndAmount(PaystubFieldNames.TS_MEDICAL_PRE_TAX);
		printFieldAndAmount(PaystubFieldNames.TS_DENTAL_PRE_TAX);
		printFieldAndAmount(PaystubFieldNames.TS_VISION_PRE_TAX);
		printFieldAndAmount(PaystubFieldNames.TS_ACCIDENT_PRE_TAX);
		printFieldAndAmount(PaystubFieldNames.TS_HOSPITAL_PRE_TAX);
		
	}
	
	private void printFieldAndAmount(String fieldName) {
		StubField field = this.fields.get(fieldName);
		
		if (field != null) {
			double currentAmount = field.getCurrent() ;
			System.out.println(field.getFieldName() + ": " + currentAmount);
		}
	}
}
