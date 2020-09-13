package com.jay.paystub2budget.workbook;

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

import com.jay.paystub2budget.paystub.Paystub;
import com.jay.paystub2budget.paystub.StubField;
import com.jay.paystub2budget.util.Months;
import com.jay.paystub2budget.util.PaystubFieldNames;

public class WorkbookProcessor {
	
	public static final String SHEET_TITLE = "PERSONAL BUDGET";
	
	File excelFile;
	XSSFWorkbook workbook;
	
	public WorkbookProcessor(File excelFile) {
		this.excelFile = excelFile;
	}
	
	public void updateWorkbook(Paystub stub) {
		// Open Excel budget workbook
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(excelFile);
			XSSFSheet sheet = workbook.getSheet(SHEET_TITLE);

			Months paymentMonth = stub.getMonth();
			XSSFRow wages = sheet.getRow(5);
			for (Row row : sheet) {
				Cell cell = row.getCell(0);
				if (cell != null
						&& cell.getCellType().equals(CellType.STRING)) {
					Cell cellToEdit = row
							.getCell(paymentMonth.getExcelColumnNum());
					double currentAmount = 0;
					double amountToAdd = 0;
					boolean equalsYearToDate = false;

					if (cell.getStringCellValue()
							.equals(PaystubFieldNames.GD_WAGES)) {
						StubField stubField = stub.getExtractedFields()
								.get(PaystubFieldNames.GD_NET_PAY);
						currentAmount = cellToEdit.getNumericCellValue();
						amountToAdd = stubField.getCurrent();
						if (stubField.getYearToDate() == stubField.getCurrent()
								+ amountToAdd) {
							equalsYearToDate = true;
						}
					} else if (cell.getStringCellValue()
							.equals(PaystubFieldNames.GD_INCOME_TAX)) {
						StubField stubField = stub.getExtractedFields()
								.get(PaystubFieldNames.GD_TOTAL_TAXES_WITHHELD);
						currentAmount = cellToEdit.getNumericCellValue();
						amountToAdd = stubField.getCurrent();
						if (stubField.getYearToDate() == stubField.getCurrent()
								+ amountToAdd) {
							equalsYearToDate = true;
						}
					} else if (cell.getStringCellValue()
							.equals(PaystubFieldNames.GD_RETIREMENT)) {
						StubField stubField = stub.getExtractedFields().get(
								PaystubFieldNames.GD_TOTAL_PRETAX_DEDUCTIONS);
						currentAmount = cellToEdit.getNumericCellValue();
						amountToAdd = stubField.getCurrent();
						if (stubField.getYearToDate() == stubField.getCurrent()
								+ amountToAdd) {
							equalsYearToDate = true;
						}
					}

					if (equalsYearToDate) {
						cellToEdit.setCellValue(currentAmount + amountToAdd);
					}
				}
			}

			// Necessary line for formulas to work
			XSSFFormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
					.createFormulaEvaluator();
			formulaEvaluator.evaluateAll();

			outputNewWorkbookFile(workbook); // For some reason, the data will
												// only save in the testBudget
												// when the newBudget xlsx file
												// is created
			workbook.close();

			System.out
					.println("Paystub 2 Budget Program finished writing to workbook successfully!");

		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
	
	private static void outputNewWorkbookFile(XSSFWorkbook workbook) {

		try {
			FileOutputStream outStream = new FileOutputStream(
					"C:\\Users\\Jacob\\Documents\\newBudget.xlsx");
			workbook.write(outStream);
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
