/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.texttoentities;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author kodi
 */
public class TextToEntities {
    private String fileName = "";
    
    public TextToEntities(String fileName){
        this.fileName = fileName;
    }
    public List<String> getRegexEntities(String text){
        List<String> entities = new ArrayList<>();
        Pattern p = Pattern.compile("([A-Z](\\.|[a-z\\/]+)[\\s\\-\\—]?(of\\s|and\\s|St\\.\\s|for\\s|in\\sthe\\s)?){2,}(\\s?\\([^\\)]+\\))?");
        Matcher m = p.matcher(text.replace("  ", " "));
        System.out.println("Regular Expression Results:");
        HashMap<String, String> mapping = new HashMap<>();
        while(m.find()){
            if(!(m.group().startsWith("Acknowled")|| m.group().startsWith("Fund"))){
                String entity = m.group().trim();
                entities.add("<ENTITY>"+entity+"</ENTITY>");
                System.out.println("<ENTITY>"+entity+"</ENTITY>");
                mapping.put(entity, "ENTITY");
            }
        }
        saveToXML(mapping, "RegEx");
        
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
            Pattern p = Pattern.compile("<(.*?)>([^<]*?)<\\/.*?>");
            Matcher m = p.matcher(inlineXML);
            System.out.println("---");
            System.out.println("Stanford NER Results:");
            HashMap<String, String> mapping = new HashMap<>();
            while(m.find()){
                entities.add(m.group());
                System.out.println(m.group());
                mapping.put(m.group(2), m.group(1));
            }
            System.out.println("---");
            
            saveToXML(mapping, "NER");
            
        } catch (IOException | ClassCastException | ClassNotFoundException ex) {
            System.out.println(ex);
        }
        return entities;
    }

    private void saveToXML(Map<String, String> mapping, String method) {
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document doc = builder.newDocument();
            Element root = doc.createElement("FUNDING");
            doc.appendChild(root);
            
            Set<String> keys = mapping.keySet();
            for(String entity : keys){
                String category = mapping.get(entity);
                Element entry = doc.createElement(category);
                entry.appendChild(doc.createTextNode(entity));
                root.appendChild(entry);
            }
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer trans = transFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("output/"+method+"_"+fileName+".xml"));
            
            trans.transform(source, result);
            
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
