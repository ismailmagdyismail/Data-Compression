package Compression.LZW;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

class LZWParser {
    static ArrayList<LZWTag> parse(String compressedTags){
        ArrayList<LZWTag> tags = new ArrayList<>();
        String[] filteredTags = compressedTags.substring(1,compressedTags.length()-1).split(",");
        for (int i = 0 ;i<filteredTags.length;i++){
            int start = 1 ;
            if(filteredTags[i].charAt(0) == ' '){
                start = 2;
            }
            int index = Integer.parseInt(filteredTags[i].substring(start,filteredTags[i].length()-1));
            tags.add(new LZWTag(index));
        }
        return tags;
    }
}