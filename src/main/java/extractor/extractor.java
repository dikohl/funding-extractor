
import java.io.File;
import java.io.IOException;
import extractor.pdftotext.PdfToText;
import extractor.texttoentities.TextToEntities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kodi
 */
public class extractor {
    public static void main(String args[]) {
        try {
            String text,entities;
            text = PdfToText.getText(new File("/Users/kodi/Springer_nothing_declared.pdf"));
            entities = TextToEntities.getEntities(text);
            entities = "done";
        } catch (IOException e) {
            System.out.print(e);
        }
    }
}
