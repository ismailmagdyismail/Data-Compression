

import CompressView.CompressionView;
import Compression.LZW.LZWCompression;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        new CompressionView();
//        System.out.println(new LZWCompression().compress("ismail"));
//        System.out.println(new LZWCompression().compress("ismailismailismailismail"));
        System.out.println(new LZWCompression().decompress("[<9>,<19>,<13>,<1>,<9>,<12>,<53>,<55>,<57>,<59>,<56>,<58>,<54>,<63>]"));
    }
}