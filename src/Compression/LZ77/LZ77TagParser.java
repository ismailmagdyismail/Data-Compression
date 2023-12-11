package Compression.LZ77;

import java.util.ArrayList;
import java.util.Stack;

class LZ77TagParser {
    private static final int LZ77_TAG_SIZE = 3;
    static ArrayList<LZ77Tag> parse(String compressedData){
        ArrayList<LZ77Tag>tags = new ArrayList<>();
        Stack<String>stack = new Stack<>();
        String tagEntry = "";
        compressedData = compressedData.substring(1, compressedData.length() - 1);
        String filterdData[] = compressedData.split(",");

        for(int i = 0; i < filterdData.length; i++){
            if(i % 3 == 0){
                int start = 2;
                if(i == 0){
                    start = 1;
                }
                filterdData[i] = filterdData[i].substring(start);
            }
            else if( (i + 1) % 3 == 0){
                filterdData[i] = filterdData[i].substring(0, filterdData[i].length() - 1);
            }
        }

        for(int i = 0 ; i<filterdData.length; i++){
            stack.push(filterdData[i]);
            if (stack.size() == LZ77_TAG_SIZE){
                String letter = stack.pop();
                String length = stack.pop();
                String offset = stack.pop();
                tags.add(new LZ77Tag(Integer.parseInt(offset),Integer.parseInt(length), letter));
            }
        }
        return tags;
    }
}
