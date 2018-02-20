package com.jay.paystub2budget.paystub;

/*
 * This class contain a single row of extracted data from the Paystub.
 */
public class StubField {
	
	private String fieldName;
	private double current;
	private double yearToDate;
	
	public StubField (String fieldName, double current, double yearToDate) {
		this.fieldName = fieldName;
		this.current = current;
		this.yearToDate = yearToDate;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public double getCurrent() {
		return current;
	}

	public void setCurrent(double current) {
		this.current = current;
	}

	public double getYearToDate() {
		return yearToDate;
	}

	public void setYearToDate(double yearToDate) {
		this.yearToDate = yearToDate;
	}
	
	@Override
	public String toString() {
		return "StubField: " + fieldName + "\n" + 
				"current: " + current + "\n" + 
				"yearToDate " + yearToDate + "\n";
	}

}
