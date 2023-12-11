package Compression.VectorQuantization;

import java.util.Vector;

public class VectorQuantizationParser {
    private VectorQuantizationParser(){}
    public static Vector<Vector<Double>> extractVectorsFromData(String data, int vectorHeight, int vectorWidth){
        Vector<Vector<Double>> doubleData = new Vector<>();
        String[] dataList = data.split("\n");
        doubleData.setSize(dataList.length);

        // convert strings to doubles
        for(int i = 0; i < dataList.length; i++){
            String[] stringArray = dataList[i].split(" ");
            Vector<Double> doubleArray = new Vector<>();
            doubleArray.setSize(stringArray.length);
            for (int j = 0; j < stringArray.length; j++) {
                doubleArray.set(j, Double.parseDouble(stringArray[j]));
            }
            doubleData.set(i, doubleArray);
        }

        int dataWidth = doubleData.get(0).size(), dataHeight = doubleData.size();
        Vector<Vector<Double>> vectors = new Vector<>();
        vectors.setSize((dataHeight* dataWidth) / (vectorHeight * vectorWidth));

        int vecNum = 0;
        for(int i = 0; i < dataHeight; i+=vectorHeight){
            for(int j = 0; j < dataWidth; j+= vectorWidth){
                Vector<Double> singleVector = new Vector<>();
                for(int h = 0; h < vectorHeight; h++){
                    for(int w = 0; w < vectorWidth; w++){
                        singleVector.add(doubleData.get(i + h).get(w + j));
                    }
                }
                vectors.set(vecNum, singleVector);
                vecNum++;
            }
        }
        return vectors;
    }

    public static int getImageHeight(String image){
        return image.split("\n").length;
    }

    public static int getImageWidth(String image){
        return image.split("\n")[0].split(" ").length;
    }

    public static int extractImageWidthFromData(String data){
        return Integer.parseInt(data.split("\n")[0]);
    }

    public static int extractImageHeightFromData(String data){
        return Integer.parseInt(data.split("\n")[1]);
    }

    public static int extractCodeBookSizeFromData(String data){
        return Integer.parseInt(data.split("\n")[2]);
    }

    public static int extractVectorWidthFromData(String data){
        return Integer.parseInt(data.split("\n")[3]);
    }

    public static int extractVectorHeightFromData(String data){
        return Integer.parseInt(data.split("\n")[4]);
    }

    public static Vector<Vector<Integer>> extractCodeBookFromData(String data){
        String[] splittedData = data.split("\n");
        Vector<Vector<Integer>> codeTable = new Vector<>();
        codeTable.setSize(extractCodeBookSizeFromData(data));
        for(int i = 0; i < codeTable.size(); i++){
            codeTable.set(i, new Vector<>());
        }
        int vectorSize = extractVectorHeightFromData(data) * extractVectorWidthFromData(data);
        int start = 5;

        for(int i = 0; i < codeTable.size(); i++){
            for(int j = 0; j < vectorSize; j++){
                codeTable.get(i).add(Integer.parseInt(splittedData[start++]));
            }
        }

        return codeTable;
    }

    public static Vector<Integer> extractCompressedImageFromData(String data){
        String[] splittedData = data.split("\n");
        int vectorSize = extractVectorHeightFromData(data) * extractVectorWidthFromData(data);
        Vector<Integer> extractedIndexedImageFromData = new Vector<>();
        extractedIndexedImageFromData.setSize(extractImageHeightFromData(data) * extractImageWidthFromData(data) / vectorSize);
        int start = (extractCodeBookSizeFromData(data) * vectorSize) + 5; // (4 * 4) + 5
        for(int i = 0; i < extractedIndexedImageFromData.size(); i++){
            extractedIndexedImageFromData.set(i, Integer.parseInt(splittedData[start++]));
        }

        return extractedIndexedImageFromData;
    }
}