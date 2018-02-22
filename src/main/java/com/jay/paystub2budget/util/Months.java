package com.jay.paystub2budget.util;

/**
 * Enum for months and their corresponding 
 * column number in the Excel spreadsheet.
 *
 */
public enum Months {
	JAN("JAN", 1),
	FEB("FEB", 2),
	MAR("MAR", 3),
	APR("APR", 4),
	MAY("MAY", 5),
	JUN("JUN", 6),
	JUL("JUL", 7),
	AUG("AUG", 8),
	SEP("SEP", 9),
	OCT("OCT", 10),
	NOV("NOV", 11),
	DEC("DEC", 12);
	
	private String value;
	private int excelColumnNum;
	
	private Months(String value, int excelColumnNum) {
		this.value = value;
		this.excelColumnNum = excelColumnNum;
	}
	
	public String getValue() {
		return value;
	}
	
	public int getExcelColumnNum() {
		return excelColumnNum;
	}
}
