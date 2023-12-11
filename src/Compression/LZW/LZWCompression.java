package Compression.LZW;

import Compression.ICompression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LZWCompression implements ICompression {
    @Override
    public String compress(String word) {
        ArrayList<LZWTag>tags = new ArrayList<>();
        Map<String,Integer>dictionary = createCompressionDictionary();
        String currentSequence = "";
        for(int i =0  ;i<word.length();){
            currentSequence += word.charAt(i);
            if(dictionary.get(currentSequence) != null){
                i++;
                continue;
            }
            int encodedIndex = dictionary.get(currentSequence.substring(0,currentSequence.length()-1));
            tags.add(new LZWTag(encodedIndex));
            dictionary.put(currentSequence,dictionary.size()+1);
            currentSequence = "";
        }
        if(!currentSequence.isEmpty()){
            int encodedIndex = dictionary.get(currentSequence);
            tags.add(new LZWTag(encodedIndex));
        }
//        String stringTags = "";
//        for (LZWTag tag : tags) {
////            stringTags += Character.toString((char) tag.index);
//            stringTags += "<";
//            stringTags += Integer.toString(tag.index);
//            stringTags += ">";
//        }
        return tags.toString();
    }

    @Override
    public String decompress(String data) {
        ArrayList<LZWTag>tags = LZWParser.parse(data);
        Map<Integer,String> dictionary = createDecompressionDictionary();
        String decompressedString =  "";
        String last ="";
        for(int i = 0 ;i<tags.size();i++){
            String repeatingSequence = "";
            if(dictionary.get(tags.get(i).index) == null){
                repeatingSequence = last + last.charAt(0);
                dictionary.put(dictionary.size()+1,repeatingSequence);
            }
            else{
                repeatingSequence = dictionary.get(tags.get(i).index);
                if(i != 0){
                    dictionary.put(dictionary.size()+1,last+repeatingSequence.charAt(0));
                }
            }
            decompressedString += repeatingSequence;
            last = repeatingSequence;
        }
        return decompressedString;
    }
    
    private Map<String,Integer>createCompressionDictionary(){
        Map<String,Integer>dictionary = new HashMap<>();
        for(int i = 1;i<=127;i++){
            dictionary.put(String.valueOf((char) i),i);
        }
        return dictionary;
    }

    private Map<Integer,String>createDecompressionDictionary(){
        Map<Integer,String>dictionary = new HashMap<>();
        for(int i = 1;i<=127;i++){
            dictionary.put(i+1,String.valueOf((char) i));
        }
        return dictionary;
    }
}
