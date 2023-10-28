package Compression.LZ77;

import Compression.ICompression;

import java.util.ArrayList;

public class LZ77Compression implements ICompression {

    @Override
    public String decompress(String data){
        ArrayList<LZ77Tag> tags = LZ77TagParser.parse(data);
        String decompressedData = "";
        for(int i = 0 ;i<tags.size();i++){
            int start = decompressedData.length() - tags.get(i).offset;
            System.out.println(start);
            int end = start + tags.get(i).length;
            decompressedData += decompressedData.substring(start,end);
            if(Character.isLetterOrDigit(tags.get(i).next)){
                decompressedData += tags.get(i).next;
            }
        }
        return decompressedData;
    }
    @Override
    public String compress(String data) {
        ArrayList<LZ77Tag> compressedTags = new ArrayList<>();
        for(int i = 0 ;i<data.length()-1;){
            LZ77Tag tag = new LZ77Tag(0,0,data.charAt(i));
            for(int j = 0; j < i; j++){
                String currentMatch = findMatchingSubString(j,i,i,data);
                int len = currentMatch.length();
                if(!currentMatch.isEmpty()){
                    tag = maxTag(tag,i-j,currentMatch.length(),i+len < data.length() ? data.charAt(i+len):'-');
                }
            }
            compressedTags.add(tag);
            i = i + tag.length + 1;
        }
        return compressedTags.toString();
    }
    private String findMatchingSubString(int l , int r,int searchBufferSize,String data){
        String currentMatch = "";
        while (l < searchBufferSize && r < data.length() && data.charAt(l) == data.charAt(r)){
            currentMatch += data.charAt(l);
            l++;
            r++;
        }
        return currentMatch;
    }
    private LZ77Tag maxTag(LZ77Tag oldTag,int newOffset,int newLength,char next){
        if(newLength >= oldTag.length){
            return new LZ77Tag(newOffset,newLength,next);
        }
        return oldTag;
    }
}
