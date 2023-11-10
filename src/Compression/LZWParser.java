package Compression.LZW;

import java.util.ArrayList;

class LZWParser {
    static ArrayList<LZWTag> parse(String compressedTags){
        /// remove []
        compressedTags = compressedTags.substring(0,compressedTags.length()-1);
        compressedTags = compressedTags.substring(1);
        /// remove ,
        String[]filteredString = compressedTags.split(",");
        /// remove <>
        ArrayList<LZWTag> tags = new ArrayList<>();
        System.out.println(filteredString);
        for (int i = 0 ;i<filteredString.length;i++){
            filteredString[i] = filteredString[i].substring(1,filteredString[i].length()-1);
            tags.add(new LZWTag(Integer.parseInt(filteredString[i])));
        }
        return tags;
    }
}
