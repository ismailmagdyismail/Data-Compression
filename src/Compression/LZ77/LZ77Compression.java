package Compression.LZ77;

import Compression.ICompression;

import java.util.ArrayList;

public class LZ77Compression implements ICompression {
    @Override
    public String compress(String data) {
        ArrayList<LZ77Tag> compressedData = new ArrayList<LZ77Tag>();
        String window = "", current = "";

        for (int i = 0; i < data.length(); i++){
            current += data.charAt(i);
            if(window.lastIndexOf(current) == -1){
                addTag(compressedData, current, window);
                window += current;
                current = "";
            }
        }
        if(!current.isEmpty()){
            addTag(compressedData, current, window);
        }
        return compressedData.toString();
    }
    @Override
    public String decompress(String data){
        ArrayList<LZ77Tag> tags = LZ77TagParser.parse(data);
        String decompressedData = "";
        for (LZ77Tag tag : tags) {
            int start = decompressedData.length() - tag.offset;
            int end = start + tag.length;
            decompressedData += decompressedData.substring(start, end);
            if(tag.next.equals("\\n")){
                decompressedData += "\n";
            }
            else if (!tag.next.equals("ً")) {
                decompressedData += tag.next;
            }
        }

        return decompressedData;
    }
    private static void addTag(ArrayList<LZ77Tag> compressedData, String current, String window){
        int length = current.length() - 1;
        String next = String.valueOf(current.charAt(current.length() - 1));
        String matchString = current.substring(0, current.length() - 1);
        int lastIndexSearch = window.lastIndexOf(matchString);
        int position = window.length() - lastIndexSearch;

        // if all chars at the current found at the window that's mean the loop has been terminated and there is remaining chars
        if(window.lastIndexOf(current) != -1){
            next = "ً";
            length += 1;
            position = window.length() - window.lastIndexOf(current);
        }

        // first appear to char
        else if(current.length() == 1) {
            position = 0;
        }

        if(next.equals("\n") ){
            next = "\\n";
        }
        compressedData.add(new LZ77Tag(position, length, next));
    }
}