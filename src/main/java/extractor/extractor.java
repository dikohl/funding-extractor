

import java.io.IOException;
import extractor.pdftotext.PdfToText;
import extractor.resultaccuracy.ResultAccuracy;
import extractor.pdftometadata.PdfToMetadata;
import extractor.texttoentities.TextToEntities;
import extractor.texttoentities.TextToTrainingSet;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                    //PdfToMetadata.getMetadata(filePath.toString());
                    try {
                        String text = PdfToText.getText(filePath.toFile());
                        List<String> nerEntities = TextToEntities.getNerEntities(text);
                        List<String> regexEntities = TextToEntities.getRegexEntities(text);
                        List<String> goldenEntities = getGoldenEntities(filePath.getFileName().toString());
                        ResultAccuracy.compareResults(goldenEntities, regexEntities, nerEntities);
                        //TextToTrainingSet.addToTrainingData(text, entities);
                    } catch (IOException ex) {
                        System.out.print("FilePath: "+ex);
                    }
                }
            });
        } catch (IOException ex) {
            System.out.print("Path walking: "+ex);
        }
    }

    private static List<String> getGoldenEntities(String inputName) {
        List<String> goldenEntities = new ArrayList<>();
        String goldenFile = inputName.replace(".pdf", "_golden.txt");
        try(Stream<Path> paths = Files.walk(Paths.get("inputs/golden"))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath) && filePath.toString().endsWith(goldenFile)) {
                    try (Stream<String> stream = Files.lines(filePath)) {
                        stream.forEach(l -> goldenEntities.add(l));
                    } catch (IOException ex) {
                        System.out.print(ex);
                    }
                }
            });
        }
        catch (IOException ex) {
            System.out.print(ex);
        }
        return goldenEntities;
    }
}