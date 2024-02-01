package Compression.ImagesPredictiveEncoding.RW;

import IO.FileIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImagePredictiveCompressionRW extends FileIO {
    public ImagePredictiveCompressionRW(String readFilePath, String writeFilePath) {
        super(readFilePath, writeFilePath);
    }

    @Override
    public String readData() {
        int[][] greyMatrix = readGrayscaleImage(readFilePath);
        if (greyMatrix != null) {
            String data = "";
            for (int[] matrix : greyMatrix) {
                for (int i : matrix) {
                    data += i + " ";
                }
                data += "\n";
            }
            return data;
        } else {
            System.out.println("Error: Unable to read the input image.");
        }
        return null;
    }

    private static int[][] readGrayscaleImage(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));

            if (image == null) {
                System.out.println("Error: Unable to read the image. Check if the file path is correct.");
                return null;
            }

            int width = image.getWidth();
            int height = image.getHeight();

            int[][] greyGrid = new int[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    int greyValue = getGreyValue(pixel);
                    greyGrid[y][x] = greyValue;
                }
            }

            return greyGrid;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int getGreyValue(int pixel) {
        return pixel & 0xFF; // 11111111 get one byte only from the pixel
    }

    @Override
    public void print(String data) {
        try {
            String[] splittedData = data.split("\n");
            FileOutputStream fileOutputStream = new FileOutputStream(super.writeFilePath);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            // add image width and height
            dataOutputStream.writeShort(Integer.parseInt(splittedData[0]));
            dataOutputStream.writeShort(Integer.parseInt(splittedData[1]));

            for(int i = 2; i < splittedData.length; i++){
                dataOutputStream.writeShort(Integer.parseInt(splittedData[i]));
            }
            dataOutputStream.flush();
            dataOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
