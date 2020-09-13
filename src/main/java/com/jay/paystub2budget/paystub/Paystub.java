package com.jay.paystub2budget.paystub;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jay.paystub2budget.util.Months;

public class Paystub {

	private Map<String, StubField> extractedFields;
	private Months month;
	private LocalDate date;	// TODO Change this to Java.time date 
	
	public Paystub () {
		this.extractedFields = new HashMap<String, StubField>();
	}

	public Map<String, StubField> getExtractedFields() {
		return extractedFields;
	}

	public void setExtractedFields(Map<String, StubField> extractedFields) {
		this.extractedFields = extractedFields;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Months getMonth() {
		return month;
	}

	public void setMonth(Months month) {
		this.month = month;
	}
}
