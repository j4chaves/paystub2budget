package com.jay.paystub2budget.paystub;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jay.paystub2budget.util.Months;

public class PayStub {

	private Map<String, StubField> extractedFields;
	private Months month;
	private Date date;
	// TODO Separate data from PaystubReader into this class
	
	public PayStub () {
		this.extractedFields = new HashMap<String, StubField>();
		this.date = new Date();
	}

	public Map<String, StubField> getExtractedFields() {
		return extractedFields;
	}

	public void setExtractedFields(Map<String, StubField> extractedFields) {
		this.extractedFields = extractedFields;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Months getMonth() {
		return month;
	}

	public void setMonth(Months month) {
		this.month = month;
	}
}
