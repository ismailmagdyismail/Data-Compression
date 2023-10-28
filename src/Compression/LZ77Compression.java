package Compression.LZ77;

import Compression.ICompression;

import java.util.ArrayList;

public class LZ77Compression implements ICompression {
    @Override
    public String compress(String data) {

    }
    @Override
    public String decompress(String data){
        ArrayList<LZ77Tag> tags = LZ77TagParser.parse(data);
        String decompressed = "decompressed";
        /**
         * TODO implement ALGO
         */
        return decompressed;
    }
}
