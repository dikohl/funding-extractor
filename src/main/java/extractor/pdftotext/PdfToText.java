/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdftotext;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author kodi
 */
public class PdfToText {
    
    public static String getText(File pdfFile) throws IOException {
        PDDocument doc = PDDocument.load(pdfFile);
        String cleanFundingText = "";
        PDFTextStripper stripper = new PDFTextStripper();
        
        stripper.setPageStart("PAGE START");
        String rawText = stripper.getText(doc);
        
        Pattern p = Pattern.compile("((?:Funding|Acknowledg(e)?ments|Co.+\\s[iI]nterests)(?:[\\r\\n]+.*?)+\\.)(?:[\\r\\n]+.*?[^\\.][\\r\\n]+)");
        Matcher m = p.matcher(rawText);
        while(m.find()){
            String fundingText = m.group(1);
            String[] lines = fundingText.split("[\\r\\n]");
            for(String line : lines){
                if(!line.startsWith(" ") && !line.startsWith("PAGE START")){
                    if(line.charAt(line.length()-1)==' ' || line.charAt(line.length()-1)=='-'){
                        cleanFundingText = cleanFundingText + line;
                    }
                    else{
                        cleanFundingText = cleanFundingText + line + ' ';
                    }
                }
            }
            cleanFundingText = cleanFundingText + ";";
        }
        return cleanFundingText;
    }
}
