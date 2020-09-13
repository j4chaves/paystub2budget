package com.jay.paystub2budget.util;

/*
 * The field names to parse data from in the paystub files.
 * TODO This should be put into a properties file at a later date. 
 */
public class PaystubFieldNames {
	
	// GD paystub field names 
	public static final String GD_NET_PAY = "Net Pay";
	public static final String GD_TOTAL_PRETAX_DEDUCTIONS = "Total Pre-tax Deductions";
	public static final String GD_TOTAL_TAXES_WITHHELD = "Total Taxes Withheld";
	public static final String GD_CHECK_DATE = "Check Date:";
	
	// GD row names
	public static final String GD_WAGES = "Wages";
	public static final String GD_INCOME_TAX = "Income tax (additional)";
	public static final String GD_RETIREMENT = "Retirement (401k, Roth IRA)";
	
	
	
	// TS Paystub field names
	public static final String TS_REGULAR_PAY = "Ragular Pay";
	public static final String TS_HOLIDAY_PAY = "Holiday Pay";
	public static final String TS_DATA_SECURITY_TRAINING_PAY = "Data Security Training Pay";
	public static final String TS_TOTAL_EARNINGS = "Total Earnings:";
	public static final String TS_TOTAL_TAX_DEDUCTIONS = "Total Tax Deductions";
	public static final String TS_TOTAL_PRETAX_DEDUCTIONS = "Total Pre-Tax Deductions";
	public static final String TS_MEDICAL_PRE_TAX = "Medical Pre Tax";
	public static final String TS_DENTAL_PRE_TAX = "Dental Pre Tax";
	public static final String TS_VISION_PRE_TAX = "Vision Pre Tax";
	public static final String TS_ACCIDENT_PRE_TAX = "Accident Pre Tax";
	public static final String TS_HOSPITAL_PRE_TAX = "Hospital Cost Protection Plan";
	
	// TS table names
	public static final String TS_EARNINGS_TABLE = "Earnings";
	public static final String TS_TAX_DEDUCTIONS_TABLE = "TAX DEDUCTIONS";
	public static final String TS_PRE_TAX_DEDUCTIONS_TABLE = "PRE-TAX DEDUCTIONS";
	public static final String TS_AFTER_TAX_DEDUCTIONS_TABLE = "AFTER-TAX DEDUCTIONS";
	
}
