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
import java.util.List;

/**
 *
 * @author kodi
 */
public class TextToFile {
    public static void addToTrainingData(String text, List<String> entities){
        String[] splittedText = text.split(" ");
        
        List<String> splittedEntities = new ArrayList();
        for(String entity : entities){
            for (String el : entity.split(" ")){
                splittedEntities.add(el);
            }
        }
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
                        out.println(word+"\t0");
                        isPrevWord = false;
                    }
                    else{
                        out.println(word+"\tORG");
                        isPrevWord = true;
                    }
                }
                else{
                    out.println(word+"\t0");
                    isPrevWord = false;
                }
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
    
}
