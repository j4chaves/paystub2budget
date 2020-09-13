package com.jay.paystub2budget.paystub2budget;

import java.io.File;

import com.jay.paystub2budget.paystub.Paystub;
import com.jay.paystub2budget.paystub.TSPaystubReader;


public class App {
	
	public static final String PAYSTUB_PATH = "C:\\Users\\Jacob\\Documents\\testPaystub.pdf"; 
	public static final String WORKBOOK_PATH = "C:\\Users\\Jacob\\Documents\\testBudget.xlsx"; // TODO 
	
	
	public static final int WAGES_ROW_INDEX = 5;
	public static final int RETIREMENT_ROW_INDEX = 78;
	public static final int INCOME_TAX_ROW_INDEX = 80;
	
    public static void main(String[] args) {
    	
    	// TODO This whole file needs to be rewritten and all the logic taken out of main
    	
    	// Get input file from command line argument
    	File paystubFile = new File(PAYSTUB_PATH);
    	
    	// Read pay stub
    	//GDPaystubReader reader = new GDPaystubReader(paystubFile);
    	TSPaystubReader reader = new TSPaystubReader(paystubFile);
    	Paystub stub = reader.createPaystubObject();
		
    	// Write to budget workbook
    	// TODO Might scrap this altogether.  
    	//var workbookProcessor = new WorkbookProcessor(new File(WORKBOOK_PATH));
    	//workbookProcessor.updateWorkbook();
    	
    	// Output paystub results to console
    }
    
    
}
