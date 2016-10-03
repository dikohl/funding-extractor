

import PdfToMetadata.PdfToMetadata;
import java.io.IOException;
import extractor.pdftotext.PdfToText;
import extractor.texttoentities.TextToEntities;
import extractor.texttoentities.TextToTrainingSet;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
        try(Stream<Path> paths = Files.walk(Paths.get("inputs/"))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".pdf")) {
                    System.out.println(filePath.toString());
                    PdfToMetadata.getMetadata(filePath.toString());
                    /*try {
                        String text = PdfToText.getText(filePath.toFile());
                        List<String> nerEntities = TextToEntities.getNerEntities(text);
                        List<String> regexEntities = TextToEntities.getRegexEntities(text);
                        //TextToTrainingSet.addToTrainingData(text, entities);
                    } catch (IOException ex) {
                        System.out.print("FilePath: "+ex);
                    }*/
                }
            });
        } catch (IOException ex) {
            System.out.print("Path walking: "+ex);
        }
    }
}
