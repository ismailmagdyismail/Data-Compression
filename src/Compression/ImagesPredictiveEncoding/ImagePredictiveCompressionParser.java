package Compression.ImagesPredictiveEncoding;

public class ImagePredictiveCompressionParser {

    public static int parseImageWidth(String image){
        return image.split("\n")[0].split(" ").length;
    }
    public static int parseImageHeight(String image){
        return image.split("\n").length;
    }
    public static int[][] parseImageMatrix(String image){
        int width = parseImageWidth(image);
        int height = parseImageHeight(image);
        int[][] imageMatrix = new int[height][width];
        String[] imageRows = image.split("\n");
        for (int i = 0; i < imageRows.length; i++){
            String[] imageCols = imageRows[i].split(" ");
            for(int j = 0; j < imageCols.length; j++){
                imageMatrix[i][j] = Integer.parseInt(imageCols[j]);
            }
        }
        return imageMatrix;
    }
}
