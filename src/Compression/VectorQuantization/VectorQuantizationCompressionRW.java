package Compression.VectorQuantization;

import IO.FileIO;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class VectorQuantizationCompressionRW extends FileIO {
    public VectorQuantizationCompressionRW(String readFilePath, String writeFilePath) {
        super(readFilePath, writeFilePath);
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
                dataOutputStream.writeByte(Integer.parseInt(splittedData[i]));
            }
            dataOutputStream.flush();
            dataOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
