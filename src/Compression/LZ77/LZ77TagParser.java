package Compression.LZ77;

import java.util.ArrayList;
import java.util.Stack;

class LZ77TagParser {
    private static final int LZ77_TAG_SIZE = 3;
    static ArrayList<LZ77Tag> parse(String compressedData){
        ArrayList<LZ77Tag>tags = new ArrayList<>();
        Stack<String>stack = new Stack<>();
        String tagEntry = "";
        for(int i = 0 ;i<compressedData.length();i++){
            if (compressedData.charAt(i) == '>' || stack.size() == LZ77_TAG_SIZE){
                stack.push(tagEntry);
                tagEntry = "";
                String letter = stack.pop();
                String length = stack.pop();
                String offset = stack.pop();
                tags.add(new LZ77Tag(Integer.parseInt(offset),Integer.parseInt(length),letter.charAt(0)));
            }else if(compressedData.charAt(i) == ',' && !tagEntry.isEmpty()){
                stack.push(tagEntry);
                tagEntry = "";
            }else  if(Character.isLetterOrDigit(compressedData.charAt(i)) || compressedData.charAt(i) == '-'){
                tagEntry += compressedData.charAt(i);
            }
        }
        return tags;
    }
}
