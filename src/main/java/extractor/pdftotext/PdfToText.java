/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdftotext;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.OutputTarget;

/**
 *
 * @author kodi
 */
public class PdfToText {
    
    public static String getText(File file) throws IOException {
        String rawText = getPdfBoxRaw(file);
        String cleanText = getCleanText(rawText);
        if(cleanText.length() < 5){
            rawText = getPdfXStreamRaw(file);
            cleanText = getCleanText(rawText);
        }
        return cleanText;
        
    }
    
    private static String getPdfBoxRaw(File file){
        try {
            PDDocument doc = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            
            stripper.setPageStart("PAGE START");
            stripper.setPageEnd("PAGE END");
            //gets the text form the doc and replaces unknown signs with \n
            String rawText = stripper.getText(doc).replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]","\n");
            doc.close();
            return rawText;
            
        } catch (IOException ex) {
            Logger.getLogger(PdfToText.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    private static String getPdfXStreamRaw(File file){
        try {
            Document doc = PDF.open(file);
            StringBuilder text = new StringBuilder(1024);
            doc.pipe(new OutputTarget(text));
            doc.close();
            return text.toString();
        } catch (IOException ex) {
            Logger.getLogger(PdfToText.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private static String getCleanText(String rawText) {
        String cleanFundingText = "";
        Pattern p = Pattern.compile("[\\.\\n\\r]?((?:Funding(s)?(/Support)?(\\ssources)?|Acknowledg(e)?ment(s)?(:)?|[cC]onflict.+\\s[iI]nterests|ACKNOWLEDG(E)?MENTS(S)?)(?:[\\r\\n]+.*?)+\\.)(?:[\\r\\n]*[A-Za-z]*(\\s[A-Za-z]*)?[\\r\\n]+)");
        Matcher m = p.matcher(rawText);
        while(m.find()){
            String fundingText = m.group(1);
            //System.out.print(fundingText+"\n");
            String[] lines = fundingText.split("[\\r\\n]");
            for(String line : lines){
                //remove the header lines
                if(!line.contains("PAGE START") && !line.contains("PAGE END")){
                    if(line.endsWith(" ")|| line.endsWith("-")){
                        cleanFundingText = cleanFundingText + line;
                    }
                    else{
                        cleanFundingText = cleanFundingText + line + ' ';
                    }
                }
            }
        }
        System.out.print(cleanFundingText+"\n");
        return cleanFundingText;
    }
}
