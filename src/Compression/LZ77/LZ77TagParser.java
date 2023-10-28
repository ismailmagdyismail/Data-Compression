package Compression.LZ77;

import java.util.ArrayList;
import java.util.Stack;

class LZ77TagParser {
    private static final int TAG_SIZE = 3;
    static ArrayList<LZ77Tag> parse(String compressedData){
        ArrayList<LZ77Tag>tags = new ArrayList<>();
        Stack<String>stack = new Stack<>();
        String lastEntry = "";
        for(int i = 0 ;i<compressedData.length();i++){
            if(!Character.isLetterOrDigit(compressedData.charAt(i))){
                continue;
            }
            if (compressedData.charAt(i) == '>' || stack.size() == TAG_SIZE){
                stack.push(lastEntry);
                lastEntry = "";
                String letter = stack.pop();
                String length = stack.pop();
                String offset = stack.pop();
                tags.add(new LZ77Tag(Integer.parseInt(offset),Integer.parseInt(length),letter.charAt(0)));
                System.out.println("tags"+tags.get(tags.size()-1));
            }else if(compressedData.charAt(i) == ','){
                stack.push(lastEntry);
                lastEntry = "";
            }else if (compressedData.charAt(i) != '<'){
                lastEntry += compressedData.charAt(i);
            }
        }
        return tags;
    }
}
