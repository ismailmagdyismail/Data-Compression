package Compression.ImagesPredictiveEncoding;

public class ImagePredictiveDecompressionParser {
    public static int parseWidth(String data){
        return Integer.parseInt(data.split("\n")[0]);
    }
    public static int parseHeight(String data){
        return Integer.parseInt(data.split("\n")[1]);
    }
    public static int[][] parseDeQuantizationDifferenceMatrix(String data){
        int width = parseWidth(data);
        int height = parseHeight(data);
        String[] splittedData = data.split("\n");
        int[][] deQuantizedDifferenceMatrix = new int[width][height];
        int currRow = 0;
        int currCol = 0;
        for(int i = 2; i < splittedData.length; i++){
            if(currCol != 0 && currRow != 0) {
                deQuantizedDifferenceMatrix[currRow][currCol] = Integer.parseInt(splittedData[i]);
            }
            currCol++;
            if(currCol == width){
                currCol = 0;
                currRow++;
            }
        }
        return deQuantizedDifferenceMatrix;
    }

    public static int[][] parseImageMatrix(String data){
        int width = parseWidth(data);
        int height = parseHeight(data);
        String[] splittedData = data.split("\n");
        int[][] imageMatrix = new int[width][height];
        int currRow = 0;
        int currCol = 0;
        for(int i = 2; i < splittedData.length; i++){
            if(currCol == 0 || currRow == 0) {
                imageMatrix[currRow][currCol] = Integer.parseInt(splittedData[i]);
            }
            currCol++;
            if(currCol == width){
                currCol = 0;
                currRow++;
            }
        }
        return imageMatrix;
    }
}
