package Compression.ImagesPredictiveEncoding;

import Compression.ICompression;

import java.util.Arrays;

import static Compression.ImagesPredictiveEncoding.ImagePredictiveCompressionParser.*;

class Range{
    public int start;
    public int end;
    public Range(int start, int end){
        this.start = start;
        this.end = end;
    }
    public int getDeQuantizedValue(){
        return start + ((end - start) / 2);
    }
}

public class ImagePredictiveCompression implements ICompression {
    int QUANTIZATION_LEVELS = 16;
    @Override
    public String compress(String data) {
        int [][] imageMatrix = parseImageMatrix(data);
        int width = parseImageWidth(data);
        int height = parseImageHeight(data);
        Range[] quantizerRanges = generateQuantizerRanges(imageMatrix);
        int[][] decodedImage = new int[height][width];
        int [][] deQuantizedDiffMatrix = new int[height][width];

        for (Range r : quantizerRanges) {
            System.out.println(r.start + " " + r.end + " " + r.getDeQuantizedValue());
        }

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                // filling the first row, column
                if(i == 0 || j == 0){
                    decodedImage[i][j] = imageMatrix[i][j];
                    continue;
                }
                int currentPrediction = getCellPrediction(i,j,decodedImage);
                int predictedDifference = imageMatrix[i][j] - currentPrediction;

                int quantizedValue = getOverlapRangeIndex(predictedDifference, quantizerRanges);
                if(quantizedValue == -1){
                    System.out.println(predictedDifference + "\n");
                }

                decodedImage[i][j] = currentPrediction + quantizerRanges[quantizedValue].getDeQuantizedValue();
                deQuantizedDiffMatrix[i][j] = quantizerRanges[quantizedValue].getDeQuantizedValue();
            }
        }
        String compressedData =  generateEncodedData(width,height,deQuantizedDiffMatrix,imageMatrix);
        System.out.println(compressedData);
        return compressedData;
    }

    @Override
    public String decompress(String data) {
        int width = ImagePredictiveDecompressionParser.parseWidth(data);
        int height = ImagePredictiveDecompressionParser.parseHeight(data);
        int[][] deQuantizationDiffMatrix = ImagePredictiveDecompressionParser.parseDeQuantizationDifferenceMatrix(data);
        int[][] imageMatrix = ImagePredictiveDecompressionParser.parseImageMatrix(data);
        int[][] decodedMatrix = new int[height][width];
        for(int i = 0 ;i<height;i++){
            for(int j = 0 ;j<width;j++){
                if(i == 0 || j == 0 ){
                    decodedMatrix[i][j] = imageMatrix[i][j];
                    continue;
                }
                int cellPrediction = getCellPrediction(i,j,decodedMatrix);
                decodedMatrix[i][j] = cellPrediction + deQuantizationDiffMatrix[i][j];
            }
        }
        String decodedImageString = "";
        for(int i = 0 ;i<height;i++){
            for(int j = 0 ;j<width;j++){
                decodedImageString += decodedMatrix[i][j];
                if(j != width -1){
                    decodedImageString += " ";
                }
            }
            if(i != height -1){
                decodedImageString += "\n";
            }
        }
        return decodedImageString;
    }

    private int getOverlapRangeIndex(int predictedDifference, Range[] quantizer) {
        int overlapRangeIndex = -1;
        for (int i = 0; i < quantizer.length; i++) {
            if(predictedDifference >= quantizer[i].start && predictedDifference <= quantizer[i].end){
                overlapRangeIndex = i;
                break;
            }
        }
        return overlapRangeIndex;
    }

    private Range[] generateQuantizerRanges(int[][] imageMatrix) {
        // for testing
//        Range[] testQuantizer = new Range[4];
//        testQuantizer[0] = new Range(-5, -3);
//        testQuantizer[1] = new Range(-2, 0);
//        testQuantizer[2] = new Range(1, 3);
//        testQuantizer[3] = new Range(4, 6);
//        return testQuantizer;
        int[][] differenceMatrix = generateDifferenceMatrix(imageMatrix);
        int[] flattenedArray = Arrays.stream(differenceMatrix)
                .flatMapToInt(Arrays::stream)
                .toArray();
        int minDiff = Arrays.stream(flattenedArray).min().getAsInt() - 50;
        int maxDiff = Arrays.stream(flattenedArray).max().getAsInt() + 50;
        int stepSize = (maxDiff - minDiff + 1) / QUANTIZATION_LEVELS;
        System.out.println(minDiff + " ||| " + maxDiff +"=>" +( maxDiff-minDiff+1));
        Range[] quantizer = new Range[QUANTIZATION_LEVELS];

        int currentRangeStart = minDiff;
        for(int i = 0; i < QUANTIZATION_LEVELS; i++){
            quantizer[i] = new Range(currentRangeStart, currentRangeStart + stepSize);
            currentRangeStart += stepSize + 1;
        }
        return quantizer;
    }
    private int[][] generateDifferenceMatrix(int[][] imageMatrix){
        int width = imageMatrix[0].length;
        int height = imageMatrix.length;
        int [][] differenceMatrix = new int[height][width];
        for(int i = 1 ;i < height;i++){
            for(int j = 1 ;j<width;j++){
                int current = getCellPrediction(i,j,imageMatrix);
                differenceMatrix[i][j] = imageMatrix[i][j] - current;
            }
        }
        return differenceMatrix;
    }
    private String generateEncodedData(int width, int height,int[][] deQuantizedDiffMatrix, int[][] imageMatrix){
        String compressedData = "";
        compressedData += width + "\n";
        compressedData += height + "\n";
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(i == 0 || j == 0) {
                    compressedData += imageMatrix[i][j];
                }
                else{
                    compressedData += deQuantizedDiffMatrix[i][j];
                }
                if(j != width - 1){
                    compressedData += "\n";
                }
            }
            if(i != height - 1){
                compressedData += "\n";
            }
        }
        return compressedData;
    }
    private int getCellPrediction(int i,int j,int[][] matrix){
        int above = matrix[i-1][j];
        int before = matrix[i][j-1];
        int diagonal = matrix[i-1][j-1];
        if(diagonal <= above && diagonal <= before){
            return Math.max(above,before);
        }else if(diagonal >= above && diagonal >= before){
            return Math.min(above,before);
        }
        return above + before - diagonal;
    }
}
