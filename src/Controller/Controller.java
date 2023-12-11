package Controller;
import Compression.Factory.CompressionFactory;
import Compression.ICompression;
import Compression.VectorQuantization.VectorQuantizationCompression;
import Compression.VectorQuantization.VectorQuantizationCompressionRW;
import Compression.VectorQuantization.VectorQuantizationDecompressionRW;
import IO.*;
import View.GUI;

public class Controller {
    private final CompressionFactory compressionFactoryInstance = CompressionFactory.getCompressionFactoryInstance();
    private final String[] algorithmsList = compressionFactoryInstance.getAvailableCompressionAlgorithms();
    private ICompression compressionAlgorithm;
    private GUI gui;

    public Controller(GUI gui) {
        this.gui = gui;
    }

    public String[] getAlgorithmsList(){
        return algorithmsList;
    }
    public void setCompressionAlgorithm(int compressionAlgorithmNumber){
        compressionAlgorithm = compressionFactoryInstance.createCompression(algorithmsList[compressionAlgorithmNumber]);
    }

    public String compress(){
        FileIO fileIO = new FileIO(gui.getReadFilePath(), gui.getWriteFilePath());

        // vector quantization customized R, W
        if(compressionAlgorithm instanceof VectorQuantizationCompression){
            fileIO = new VectorQuantizationCompressionRW(gui.getReadFilePath(), gui.getWriteFilePath());
        }

        String data = fileIO.readData();
        String compressedData = compressionAlgorithm.compress(data);
        fileIO.print(compressedData);
        return compressedData;
    }
    public String decompress(){
        FileIO fileIO = new FileIO(gui.getReadFilePath(), gui.getWriteFilePath());

        // vector quantization customized R, W
        if(compressionAlgorithm instanceof VectorQuantizationCompression){
            fileIO = new VectorQuantizationDecompressionRW(gui.getReadFilePath(), gui.getWriteFilePath());
        }

        String data = fileIO.readData();
        String deCompressedData = compressionAlgorithm.decompress(data);
        fileIO.print(deCompressedData);
        return deCompressedData;
    }
}
