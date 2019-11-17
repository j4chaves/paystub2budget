package com.jay.paystub2budget.paystub2budget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jay.paystub2budget.paystub.PayStub;
import com.jay.paystub2budget.paystub.PayStubReader;
import com.jay.paystub2budget.util.Months;


public class App {
	
	public static final String PAYSTUB_PATH = "D:\\Paystubs\\PayStub Jan 2nd stub 2018.pdf"; 
	public static final String WORKBOOK_PATH = "C:\\Users\\Jacob\\Documents\\testBudget.xlsx"; // TODO
	public static final String SHEET_TITLE = "PERSONAL BUDGET";
	
	public static final int WAGES_ROW_INDEX = 5;
	public static final int RETIREMENT_ROW_INDEX = 78;
	public static final int INCOME_TAX_ROW_INDEX = 80;
	
	//Row names
	public static final String WAGES = "Wages";
	public static final String INCOME_TAX = "Income tax (additional)";
	public static final String RETIREMENT = "Retirement (401k, Roth IRA)";
	
    public static void main( String[] args ) {
    	
    	// TODO Thid whole file needs to be rewritten and all the logic taken out of main
    	
    	// Get input file from command line argument
    	File paystubFile = new File(PAYSTUB_PATH);
    	
    	// Read pay stub
    	PayStubReader reader = new PayStubReader(paystubFile);
    	PayStub stub = reader.createPayStubObject();
		
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
	    			if(cell.getStringCellValue().equals(WAGES)) {
	    				Cell cellToEdit = row.getCell(paymentMonth.getExcelColumnNum());
	    				Double currentAmount = cellToEdit.getNumericCellValue();
	    				Double amountToAdd = stub.getExtractedFields().get(PayStubReader.NET_PAY).getCurrent();
	    				cellToEdit.setCellValue(currentAmount + amountToAdd);
	    			} else if (cell.getStringCellValue().equals(INCOME_TAX)) {
	    				Cell cellToEdit = row.getCell(paymentMonth.getExcelColumnNum());
	    				Double currentAmount = cellToEdit.getNumericCellValue();
	    				Double amountToAdd = stub.getExtractedFields().get(PayStubReader.TOTAL_TAXES_WITHHELD).getCurrent();
	    				cellToEdit.setCellValue(currentAmount + amountToAdd);
	    			} else if (cell.getStringCellValue().equals(RETIREMENT)) {
	    				Cell cellToEdit = row.getCell(paymentMonth.getExcelColumnNum());
	    				Double currentAmount = cellToEdit.getNumericCellValue();
	    				Double amountToAdd = stub.getExtractedFields().get(PayStubReader.TOTAL_PRETAX_DEDUCTIONS).getCurrent();
	    				cellToEdit.setCellValue(currentAmount + amountToAdd);
	    			}
	    		}
	    	}
	    	
	    	// Necessary line for formulas to work
	    	XSSFFormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
	    	formulaEvaluator.evaluateAll();
	    	
	    	//outputNewWorkbookFile(workbook);
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
    
    private void outputNewWorkbookFile(XSSFWorkbook workbook) {
    	
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
