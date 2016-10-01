

import java.io.IOException;
import extractor.pdftotext.PdfToText;
import extractor.texttoentities.TextToEntities;
import extractor.texttoentities.TextToFile;

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
                    try {
                        System.out.println(filePath);
                        String text = "";
                        text = PdfToText.getText(filePath.toFile());
                        List<String> entities = TextToEntities.getEntities(text);
                        TextToFile.addToTrainingData(text, entities);
                    } catch (IOException ex) {
                        System.out.print("FilePath: "+ex);
                    }

                }
            });
        } catch (IOException ex) {
            System.out.print("Path walking: "+ex);
        }
    }
}
