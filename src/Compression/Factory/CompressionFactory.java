package Compression.Factory;

import Compression.ICompression;
import Compression.LZ77.LZ77Compression;

import java.util.HashMap;
import java.util.Map;

/**
 * SINGLETON Instance , encapsulate Compression algorithm creation
 */
public class CompressionFactory {
    private final Map<String,ICompression> algorithmMapper = new HashMap<>();
    private static CompressionFactory instance = null;
    public static CompressionFactory getCompressionFactoryInstance(){
        if(CompressionFactory.instance == null){
            CompressionFactory.instance = new CompressionFactory();
        }
        return CompressionFactory.instance;
    }
    private CompressionFactory(){
        algorithmMapper.put("lz77",new LZ77Compression());
    }
    public ICompression createCompression(String algorithmName){
        return algorithmMapper.get(algorithmName);
    }
    public String[] getAvailableCompressionAlgorithms(){
        return this.algorithmMapper.keySet().toArray(new String[0]);
    }
}
