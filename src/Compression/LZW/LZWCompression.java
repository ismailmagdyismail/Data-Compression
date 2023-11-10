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
        return tags.toString();
    }
    @Override
    public String decompress(String data) {
        ArrayList<LZWTag>tags = LZWParser.parse(data);
        Map<Integer,String> dictionary = createDecompressionDictionary();
        String decompressedString =  "";
        String last ="";
        for(int i = 0 ;i<tags.size();i++){
            String repeatingSequence = dictionary.get(tags.get(i).index);
            decompressedString += repeatingSequence;
            if(i != 0){
                dictionary.put(dictionary.size()+1,last+repeatingSequence.charAt(0));
            }
            last = repeatingSequence;
        }
        return decompressedString;
    }
    private Map<String,Integer>createCompressionDictionary(){
        Map<String,Integer>dictionary = new HashMap<>();
        int index = 0 ;
        for(char i = 'a';i<='z';i++){
            index++;
            dictionary.put(String.valueOf(i),index);
        }
        for(char i = 'A';i<='Z';i++){
            index++;
            dictionary.put(String.valueOf(i),index);
        }
        return dictionary;
    }
    private Map<Integer,String>createDecompressionDictionary(){
        Map<Integer,String>dictionary = new HashMap<>();
        int index = 0 ;
        for(char i = 'a';i<='z';i++){
            index++;
            dictionary.put(index,String.valueOf(i));
        }
        for(char i = 'A';i<='Z';i++){
            index++;
            dictionary.put(index,String.valueOf(i));
        }
        return dictionary;
    }
}
