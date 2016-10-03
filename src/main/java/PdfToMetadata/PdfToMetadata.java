/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PdfToMetadata;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.grobid.core.*;
import org.grobid.core.data.*;
import org.grobid.core.factory.*;
import org.grobid.core.mock.*;
import org.grobid.core.utilities.*;
import org.grobid.core.engines.Engine;

/**
 *
 * @author kodi
 */
public class PdfToMetadata {
    public static void getMetadata(String pdfPath){
        try {
            String pGrobidHome = "grobid/grobid-home";
            String pGrobidProperties = "grobid/grobid-home/config/grobid.properties";
            
            MockContext.setInitialContext(pGrobidHome, pGrobidProperties);
            GrobidProperties.getInstance();
            
            System.out.println(">>>>>>>> GROBID_HOME="+GrobidProperties.get_GROBID_HOME_PATH());
            
            Engine engine = GrobidFactory.getInstance().createEngine();
            
            // Biblio object for the result
            BiblioItem resHeader = new BiblioItem();
            String tei = engine.processHeader(pdfPath, false, resHeader);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        finally {
            try {
                MockContext.destroyInitialContext();
            } catch (Exception ex) {
                    ex.printStackTrace();
            }
        }
    }
}
