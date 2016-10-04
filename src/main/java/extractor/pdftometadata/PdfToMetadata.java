/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdftometadata;

import java.io.FileInputStream;
import java.util.Properties;
import org.grobid.core.data.BiblioItem;
import org.grobid.core.engines.Engine;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.mock.MockContext;
import org.grobid.core.utilities.GrobidProperties;

/**
 *
 * @author kodi
 */
public class PdfToMetadata {
    private static Engine engine;
    
    public static void getMetadata(String pdfPath){
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("extractor.properties"));
            String pGrobidHome = prop.getProperty("extractor.pGrobidHome");
            String pGrobidProperties = prop.getProperty("extractor.pGrobidProperties");
            
            MockContext.setInitialContext(pGrobidHome, pGrobidProperties);
            GrobidProperties.getInstance();
            
            System.out.println(">>>>>>>> GROBID_HOME="+GrobidProperties.get_GROBID_HOME_PATH());
            
            engine = GrobidFactory.getInstance().createEngine();
            
            // Biblio object for the result
            BiblioItem resHeader = new BiblioItem();
            String tei = engine.processHeader(pdfPath, false, resHeader);
            System.out.print(tei);
            
        } catch (Exception ex) {
            System.out.println(ex);
        }
        finally {
            try {
                MockContext.destroyInitialContext();
            } catch (Exception ex) {
                    System.out.print(ex);
            }
        }
    }
}