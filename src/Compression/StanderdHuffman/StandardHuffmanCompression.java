package Compression.StanderdHuffman;

import Compression.ICompression;

import java.util.Map;

public class StandardHuffmanCompression implements ICompression {
    @Override
    public String compress(String data) {
        StandardHuffmanTree standardHuffmanTree = new StandardHuffmanTree(data);
        Map<Character, String> codeTable = standardHuffmanTree.getCodeTable();
        return encodeToBinary(data,codeTable);
    }
    @Override
    public String decompress(String data) {
        String decompressedData = "";
        String compressedStream = StandardHuffmanParser.getCompressedStream(data);
        Map<String,Character> tableCodes = StandardHuffmanParser.getTableCodes(data);
        String currentCode = "";
        for(int i = 0 ;i<compressedStream.length();i++){
            currentCode += compressedStream.charAt(i);
            if(tableCodes.get(currentCode) != null){
                decompressedData += tableCodes.get(currentCode);
                currentCode = "";
            }
        }
        return decompressedData;
    }
    private String encodeToBinary(String data , Map<Character,String> codeTable){
        String binaryData = "";
        for(int i = 0 ;i<data.length();i++){
            binaryData += codeTable.get(data.charAt(i));
        }
        binaryData += '\n';
        for(var entry : codeTable.entrySet()){
            binaryData += Integer.toBinaryString(entry.getKey());
            binaryData += entry.getValue();
            binaryData += '\n';
        }
        return binaryData;
    }
}
