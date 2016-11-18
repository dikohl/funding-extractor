/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.texttoentities;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author kodi
 */
public class TextToEntities {
    public List<String> getRegexEntities(String text){
        List<String> entities = new ArrayList<>();
        Pattern p = Pattern.compile("([A-Z](\\.|[a-z\\/]+)[\\s\\-\\—]?(of\\s|and\\s|St\\.\\s|for\\s|in\\sthe\\s)?){2,}(\\s?\\([^\\)]+\\))?");
        Matcher m = p.matcher(text.replace("  ", " "));
        System.out.println("Regular Expression Results:");
        while(m.find()){
            if(!(m.group().startsWith("Acknowled")|| m.group().startsWith("Fund"))){
                entities.add("<ENTITY>"+m.group()+"</ENTITY>");
                System.out.println("<ENTITY>"+m.group()+"</ENTITY>");
            }
        }
        System.out.println("---");
        return entities;
    }
    
    public List<String> getNerEntities(String input){
        List<String> entities = new ArrayList<>();
        //standard english classifier
        //String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
        //trained model for funding entities
        String serializedClassifier = "classifiers/ner-model10.ser.gz";
        AbstractSequenceClassifier<CoreLabel> classifier;
        try {
            classifier = CRFClassifier.getClassifier(serializedClassifier);

            String inlineXML = classifier.classifyWithInlineXML(input);
            Pattern p = Pattern.compile("<.*?>[^<]*?<\\/.*?>");
            Matcher m = p.matcher(inlineXML);
            System.out.println("---");
            System.out.println("Stanford NER Results:");
            while(m.find()){
                entities.add(m.group());
                System.out.println(m.group());
            }
            System.out.println("---");
        } catch (IOException | ClassCastException | ClassNotFoundException ex) {
            System.out.println(ex);
        }
        return entities;
    }
}
