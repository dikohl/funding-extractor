/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.texttoentities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author kodi
 */
public class TextToEntities {
    public static List<String> getEntities(String text){
        String [] arr = text.split(" ", 2);
        List<String> entities = new ArrayList<String>();
        Pattern p = Pattern.compile("([A-Z](\\.|[a-z\\/]+)[\\s\\-\\—]?(of\\s|and\\s|St\\.\\s|for\\s|in\\sthe\\s)?){2,}(\\s?\\([^\\)]+\\))?");
        Matcher m = p.matcher(arr[1]);
        while(m.find()){
            entities.add(m.group());
        }
        return entities;
    }
}
