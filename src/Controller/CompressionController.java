package Controller;

import CompressView.CompressionView;
import Compression.Factory.CompressionFactory;

public class CompressionController {
    private static final CompressionFactory compressionFactoryInstance = CompressionFactory.getCompressionFactoryInstance();
    private static final String[] algorithmsList = compressionFactoryInstance.getAvailableCompressionAlgorithms();
    private static CompressionView compressionView = new CompressionView();

}
