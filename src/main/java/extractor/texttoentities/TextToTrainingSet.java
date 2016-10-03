/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.texttoentities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author kodi
 */
public class TextToTrainingSet {
    public static void addToTrainingData(String text, List<String> entities){
        String[] splittedText = text.split(" ");
        
        List<String> splittedEntities = new ArrayList();
        entities.stream().forEach((entity) -> {
            splittedEntities.addAll(Arrays.asList(entity.split(" ")));
        });
        try(FileWriter fw = new FileWriter("trainingData.tsv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            boolean isPrevWord = false;
            for(String word : splittedText){
                word = word.replace(",","");
                if(word.length() > 2)
                    word = word.replace(".","");
                if(splittedEntities.contains(word)){
                    if(!isPrevWord&&(word.equals("of")||word.equals("the")||word.equals("in")||word.equals("and")||word.equals("for"))){
                        out.println(word+"\tO");
                        isPrevWord = false;
                    }
                    else{
                        out.println(word+"\tORGANIZATION");
                        isPrevWord = true;
                    }
                }
                else{
                    out.println(word+"\tO");
                    isPrevWord = false;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
}
