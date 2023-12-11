package Compression.StanderdHuffman;

import java.util.HashMap;
import java.util.Map;

public class StandardHuffmanParser {
    private static final int CHAR_LENGTH = 7;
    private static final int BASE = 2;
    public static String getCompressedStream(String compressedData){
        String[] data = compressedData.split("\n");
        return data[0];
    }
    public static Map<String, Character> getTableCodes(String compressedData){
        Map<String, Character> tableCodes = new HashMap<>();
        String[] data = compressedData.split("\n");
        for(int i = 1 ;i<data.length;i++){
            Character letter = (char)Integer.parseInt(data[i].substring(0,CHAR_LENGTH), BASE);
            String code = data[i].substring(CHAR_LENGTH);
            tableCodes.put(code,letter);
        }
        return tableCodes;
    }
}
