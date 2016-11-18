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
    public void addToTrainingData(String text, List<String> entities){
        String[] splittedText = text.replace("  "," ").split(" ");
        if(splittedText.length < 2){
            return;
        }
        
        List<String> splittedEntities = new ArrayList();
        entities.stream().forEach((entity) -> {
            splittedEntities.addAll(Arrays.asList(entity.substring(8,entity.length()-9).split(" ")));
        });
        try(FileWriter fw = new FileWriter("trainingData.tsv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            boolean isPrevWord = false;
            for(String word : splittedText){
                String wordRep = word.replace(",","");
                if(word.length() > 2)
                    wordRep = wordRep.replace(".","");
                if(splittedEntities.contains(wordRep)){
                    if(!isPrevWord&&(word.equals("of")||word.equals("the")||word.equals("in")||word.equals("and")||word.equals("for"))){
                        out.println(word+"\tO");
                        isPrevWord = false;
                    }
                    else{
                        if(word.endsWith(".")){
                            out.println(word.substring(0,word.length()-1)+"\tORGANIZATION");
                            out.println(".\tO");
                        }
                        else if(word.endsWith(",")){
                            out.println(word.substring(0,word.length()-1)+"\tORGANIZATION");
                            out.println(",\tO");
                        }
                        else{
                            out.println(word+"\tORGANIZATION");
                            isPrevWord = true;
                        }
                    }
                }
                else{
                    if(word.endsWith(".")){
                            out.println(word.substring(0,word.length()-1)+"\tO");
                            out.println(".\tO");
                    }
                    else if(word.endsWith(",")){
                        out.println(word.substring(0,word.length()-1)+"\tO");
                        out.println(",\tO");
                    }
                    else{
                        out.println(word+"\tO");
                        isPrevWord = false;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
}
