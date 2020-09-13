package com.jay.paystub2budget.paystub2budget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jay.paystub2budget.paystub.GDPaystubReader;
import com.jay.paystub2budget.paystub.Paystub;
import com.jay.paystub2budget.paystub.StubField;
import com.jay.paystub2budget.paystub.TSPaystubReader;
import com.jay.paystub2budget.util.Months;
import com.jay.paystub2budget.util.PaystubFieldNames;


public class App {
	
	public static final String PAYSTUB_PATH = "C:\\Users\\Jacob\\Documents\\testPaystub.pdf"; 
	public static final String WORKBOOK_PATH = "C:\\Users\\Jacob\\Documents\\testBudget.xlsx"; // TODO 
	public static final String SHEET_TITLE = "PERSONAL BUDGET";
	
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
		
    	// Open Excel budget workbook
    	File excelFile = new File(WORKBOOK_PATH);
    	XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(excelFile);
	    	XSSFSheet sheet = workbook.getSheet(SHEET_TITLE);
	    	

			Months paymentMonth = stub.getMonth();
	    	XSSFRow wages = sheet.getRow(5);	
	    	for(Row row : sheet) {
	    		Cell cell = row.getCell(0);
	    		if(cell != null && cell.getCellType().equals(CellType.STRING)) {
	    			Cell cellToEdit = row.getCell(paymentMonth.getExcelColumnNum());
    				double currentAmount = 0;
    				double amountToAdd = 0;
    				boolean equalsYearToDate = false;
	    			
	    			if(cell.getStringCellValue().equals(PaystubFieldNames.GD_WAGES)) {
	    				StubField stubField = stub.getExtractedFields().get(PaystubFieldNames.GD_NET_PAY);
	    				currentAmount = cellToEdit.getNumericCellValue();
	    				amountToAdd = stubField.getCurrent();
	    				if (stubField.getYearToDate() == stubField.getCurrent() + amountToAdd) {
	    					equalsYearToDate = true;
	    				}
	    			} else if (cell.getStringCellValue().equals(PaystubFieldNames.GD_INCOME_TAX)) {
	    				StubField stubField = stub.getExtractedFields().get(PaystubFieldNames.GD_TOTAL_TAXES_WITHHELD);
	    				currentAmount = cellToEdit.getNumericCellValue();
	    				amountToAdd = stubField.getCurrent();
	    				if (stubField.getYearToDate() == stubField.getCurrent() + amountToAdd) {
	    					equalsYearToDate = true;
	    				}
	    			} else if (cell.getStringCellValue().equals(PaystubFieldNames.GD_RETIREMENT)) {
	    				StubField stubField = stub.getExtractedFields().get(PaystubFieldNames.GD_TOTAL_PRETAX_DEDUCTIONS);
	    				currentAmount = cellToEdit.getNumericCellValue();
	    				amountToAdd = stubField.getCurrent();
	    				if (stubField.getYearToDate() == stubField.getCurrent() + amountToAdd) {
	    					equalsYearToDate = true;
	    				}
	    			}
	    			
	    			if (equalsYearToDate) {
    					cellToEdit.setCellValue(currentAmount + amountToAdd);
    				}
	    		}
	    	}
	    	
	    	// Necessary line for formulas to work
	    	XSSFFormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
	    	formulaEvaluator.evaluateAll();
	    	
	    	outputNewWorkbookFile(workbook);	//For some reason, the data will only save in the testBudget when the newBudget xlsx file is created
			workbook.close();

			System.out.println("Paystub 2 Budget Program finished successfully!");
	    	
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
    	
    	// Write to budget workbook
    }
    
    private static void outputNewWorkbookFile(XSSFWorkbook workbook) {
    	
		try {
			FileOutputStream outStream = new FileOutputStream("C:\\Users\\Jacob\\Documents\\newBudget.xlsx");
			workbook.write(outStream);
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
