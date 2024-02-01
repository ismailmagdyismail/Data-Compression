package Compression.VectorQuantization;

import Compression.ICompression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class VectorQuantizationCompression implements ICompression {
    private final int codeBookSize = 32;     // 2^n only numbers
    private final int vectorHeight = 2;
    private final int vectorWidth = 2;
    @Override
    public String compress(String data) {
        Vector<Vector<Double>> extractedVectors = VectorQuantizationParser.extractVectorsFromData(data, vectorHeight, vectorWidth);
        // root vector to start splitting
        Vector<Double> avgVector = getAvgVector(extractedVectors);
        // create code book
        Vector<Vector<Double>> codeBook = createCodeBook(extractedVectors, avgVector);
        // map each vector to it's near code book vector by indices
        Vector<Vector<Double>>[] indexedVectors = getIndexedVectors(extractedVectors, codeBook);

        int[] compressedImage = new int[extractedVectors.size()];
        // search at each index of indexed vector to find the current vector at image vectors and if it found
        // that means it has this current index
        for(int i = 0; i < extractedVectors.size(); i++){
            int vectorIndex = -1;
            boolean found = false;
            for(int j = 0; j < indexedVectors.length; j++){
                for(int v = 0; v < indexedVectors[j].size(); v++){
                    if(extractedVectors.get(i) == indexedVectors[j].get(v)){
                        vectorIndex = j;
                        found = true;
                        break;
                    }
                    if(found){
                        break;
                    }
                }
            }
            compressedImage[i] = vectorIndex;
        }

        // create saving schema
        String compressedData = "";
        compressedData += VectorQuantizationParser.getImageWidth(data) + "\n";
        compressedData += VectorQuantizationParser.getImageHeight(data) + "\n";
        compressedData += codeBookSize + "\n";
        compressedData += vectorWidth + "\n";
        compressedData += vectorHeight + "\n";
        for(int i = 0; i < codeBookSize; i++){
            for(int j = 0; j < codeBook.get(i).size(); j++){
                compressedData += codeBook.get(i).get(j).intValue() + "\n";
            }
        }

        for(int i = 0; i < compressedImage.length; i++){
            compressedData += compressedImage[i];
            if(i != compressedImage.length - 1){
                compressedData += "\n";
            }
        }

        // for debug
//        System.out.println(compressedData);
        return compressedData;
    }

    @Override
    public String decompress(String data) {
        int imageHeight = VectorQuantizationParser.extractImageHeightFromData(data);
        int imageWidth = VectorQuantizationParser.extractImageWidthFromData(data);
        int codeBookSize = VectorQuantizationParser.extractCodeBookSizeFromData(data);
        int vectorHeight = VectorQuantizationParser.extractVectorHeightFromData(data);
        int vectorWidth = VectorQuantizationParser.extractVectorWidthFromData(data);
        Vector<Vector<Integer>> codeBook = VectorQuantizationParser.extractCodeBookFromData(data);
        Vector<Integer> compressedImage = VectorQuantizationParser.extractCompressedImageFromData(data);

        Integer[][] image = new Integer[imageHeight][imageWidth];
        // compressed image vector loop
        for(int c = 0; c < compressedImage.size(); c++) {
            // fill each vector in the image
            int vIndex = 0;
            int x = (c / (imageHeight / vectorHeight) ) * vectorHeight;
            int y = (c % (imageWidth / vectorWidth)) * vectorWidth;

            for (int i = 0; i < vectorHeight; i++) {
                for (int j = 0; j < vectorWidth; j++) {
                    image[x + i][y + j] = codeBook.get(compressedImage.get(c)).get(vIndex++);
                }
            }
        }
        String decompressedData = "";
        for(int i = 0; i < imageHeight; i++){
            for(int j = 0; j < imageWidth; j++){
                decompressedData += image[i][j];
                if(j != imageWidth - 1)
                    decompressedData += " ";
            }
            if(i != imageHeight - 1)
                decompressedData += "\n";
        }
        return decompressedData;
    }


    private Vector<Vector<Double>> createCodeBook(Vector<Vector<Double>> vectors, Vector<Double> avgVector){
        Vector<Vector<Double>> codeBook = new Vector<>();
        codeBook.add(avgVector);
        Vector<Vector<Double>>[] indexedVectors;

        int numberOfLevels = (int) (Math.log(codeBookSize) / Math.log(2));
        // splitting
        for(int l = 1; l <= numberOfLevels; l++){
            Vector<Vector<Double>> splittedVectors = new Vector<>();
            for (int i = 0; i < codeBook.size(); i++) {
                ArrayList<Vector<Double>> splittedVector = split(codeBook.get(i));
                splittedVectors.add(splittedVector.get(0));
                splittedVectors.add(splittedVector.get(1));
            }

            // mapping each vector to it's nearest code vector index
            indexedVectors = getIndexedVectors(vectors, splittedVectors);

            Vector<Vector<Double>> newCodeBook = new Vector<>();
            for(int i = 0; i < indexedVectors.length; i++){
                newCodeBook.add(getAvgVector(indexedVectors[i]));
            }
            codeBook = newCodeBook;
        }

        // check if there is a change to stop or complete the iterations
        while (true){
            indexedVectors = getIndexedVectors(vectors, codeBook);
            Vector<Vector<Double>> newCodeBook = new Vector<>();
            for(int i = 0; i < indexedVectors.length; i++){
                newCodeBook.add(getAvgVector(indexedVectors[i]));
            }

            if(areVectorsIdentical(codeBook, newCodeBook)){
                break;
            } else {
                codeBook = newCodeBook;
            }
        }
        return codeBook;
    }


    private Vector<Vector<Double>>[] getIndexedVectors(Vector<Vector<Double>> vectors, Vector<Vector<Double>> codeBook){
        Vector<Vector<Double>>[] indexedVectors = new Vector[codeBook.size()];
        for (int i = 0; i < indexedVectors.length; i++) {
            indexedVectors[i] = new Vector<>();
        }

        for(int i = 0; i < vectors.size(); i++){
            int nearestIndex = findNearestCodeVectorIndex(vectors.get(i), codeBook);
            indexedVectors[nearestIndex].add(vectors.get(i));
        }
        return  indexedVectors;
    }

    private static boolean areVectorsIdentical(Vector<Vector<Double>> vectors1, Vector<Vector<Double>> vectors2) {
        if (vectors1.size() != vectors2.size()) {
            return false;
        }
        for (int i = 0; i < vectors1.size(); i++) {
            Vector<Double> vector1 = vectors1.get(i);
            Vector<Double> vector2 = vectors2.get(i);
            if (vector1.size() != vector2.size()) {
                return false;
            }
            for (int j = 0; j < vector1.size(); j++) {
                if (!vector1.get(j).equals(vector2.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private Vector<Double> getAvgVector(Vector<Vector<Double>> vectors){
        if (vectors.size() == 0)
            return null;
        Vector<Double> avgVector = new Vector<>();
        avgVector.setSize(vectors.get(0).size());
        avgVector.replaceAll(ignored -> 0.0);

        for(int i = 0; i < vectors.size(); i++){
            for(int j = 0; j < vectors.get(i).size(); j++){
                // accumulate all vectors into avgVector , each element index to it's corresponding there
                avgVector.set(j, avgVector.get(j) + vectors.get(i).get(j));
            }
        }

        for(int i = 0; i < avgVector.size(); i++){
            avgVector.set(i, avgVector.get(i) / vectors.size());
        }
        return avgVector;
    }

    private static int findNearestCodeVectorIndex(Vector<Double> vector, Vector<Vector<Double>> codebook) {
        int nearestIndex = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < codebook.size(); i++) {
            double distance = calculateDistance(vector, codebook.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                nearestIndex = i;
            }
        }
        return nearestIndex;
    }

    private static double calculateDistance(Vector<Double> vector1, Vector<Double> vector2) {
        double sum = 0;
        for (int i = 0; i < vector1.size(); i++) {
            sum += Math.abs(vector1.get(i) - vector2.get(i));
        }
        return sum;
    }

    private ArrayList<Vector<Double>> split(Vector<Double> v){
        Vector<Double> v1 = new Vector<>(), v2 = new Vector<>();
        ArrayList<Vector<Double>> splittedVectors = new ArrayList<>();
        for(int i = 0; i < v.size(); i++){
            if(v.get(i) == v.get(i).intValue()){
                v1.add((double) (v.get(i).intValue() -1));
                v2.add((double) (v.get(i).intValue() +1));
            }
            else{
                v1.add(Math.floor(v.get(i)));
                v2.add(Math.ceil(v.get(i)));
            }
        }

        splittedVectors.add(v1);
        splittedVectors.add(v2);
        return splittedVectors;
    }
}