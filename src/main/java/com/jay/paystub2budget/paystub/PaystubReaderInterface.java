package com.jay.paystub2budget.paystub;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public interface PaystubReaderInterface {

	public Paystub createPaystubObject();
	
	default public String readPaystubFile(File file) {
		
		String contents = "";

		if (file != null || !file.exists()) {
			//contents = "";	// TODO Handle better
		}
		
		PDDocument doc;
		
		try {
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setLineSeparator("\n");
			stripper.setStartPage(1);
			stripper.setEndPage(1);
			doc = PDDocument.load(file);
			contents = stripper.getText(doc);
			doc.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return contents;
	}
	
}
