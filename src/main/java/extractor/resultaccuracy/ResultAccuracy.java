/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.resultaccuracy;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kodi
 */
public class ResultAccuracy {
    public static void compareResults(List<String> goldenNer, List<String> regex, List<String> ner){
        List<String> goldenRegex = new ArrayList<>();
        System.out.println("Golden standard:");
        //build the Regex golden standard form the file (because regex doesn't differentiate entities)
        goldenNer.stream().forEach((res) -> {
            System.out.println(res);
            String tmp = res.replaceAll("ORGANIZATION", "ENTITY");
            tmp = tmp.replaceAll("PERSON", "ENTITY");
            goldenRegex.add(tmp.replaceAll("LOCATION", "ENTITY"));
        });
        System.out.println("---");
        closeMatches(regex,ner,goldenNer,goldenRegex);
        
        System.out.println("Exact matches with golden standard:");
        regex.retainAll(goldenRegex);
        double regexAccuracy = (double)regex.size()/(double)goldenRegex.size();
        System.out.println("RegEx: "+regexAccuracy*100+"%");
        ner.retainAll(goldenNer);
        double nerAccuracy = (double)ner.size()/(double)goldenNer.size();
        System.out.println("NER: "+nerAccuracy*100+"%");
    }
    private static void closeMatches(List<String> regex,List<String> ner,List<String> goldenNer,List<String> goldenRegex){
        System.out.println("Matches containing golden standard:");
        double closeRegexMatch = 0.0;
        for(String entity : regex){
            String noTags = entity.substring(entity.indexOf(">")+1, entity.substring(1, entity.length()).indexOf("<")+1);
            for(String goldenEntity : goldenRegex){
                String goldenNoTags = goldenEntity.substring(goldenEntity.indexOf(">")+1, goldenEntity.substring(1, goldenEntity.length()).indexOf("<")+1);
                if(noTags.contains(goldenNoTags)){
                    closeRegexMatch++;
                    break;
                }
            }
        }
        System.out.println("RegEx close: "+closeRegexMatch/(double)goldenRegex.size()*100+"%");
        
        double closeNerMatch = 0.0;
        for(String entity : ner){
            String noTags = entity.substring(entity.indexOf(">")+1, entity.substring(1, entity.length()).indexOf("<")+1);
            for(String goldenEntity : goldenNer){
                String goldenNoTags = goldenEntity.substring(goldenEntity.indexOf(">")+1, goldenEntity.substring(1, goldenEntity.length()).indexOf("<")+1);
                if(noTags.contains(goldenNoTags) && entity.substring(0, 3).equals(goldenEntity.substring(0,3))){
                    closeNerMatch++;
                    break;
                }
            }
        }
        System.out.println("NER close: "+closeNerMatch/(double)goldenNer.size()*100+"%");
    }
}