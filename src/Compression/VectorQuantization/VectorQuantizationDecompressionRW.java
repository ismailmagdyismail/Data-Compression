package Compression.VectorQuantization;

import IO.FileIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class VectorQuantizationDecompressionRW extends FileIO {
    public VectorQuantizationDecompressionRW(String readFilePath, String writeFilePath) {
        super(readFilePath, writeFilePath);
    }

    @Override
    public String readData() {
        String data = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(readFilePath);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            data += dataInputStream.readShort() + "\n";
            data += dataInputStream.readShort() + "\n";
            while (dataInputStream.available() > 0){
                data += dataInputStream.readByte() + "\n";
            }
            dataInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {}
        return data;
    }
}