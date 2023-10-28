package Printer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FilePrinter implements IPrinter {
    String fileName;
    public FilePrinter(String fileName) throws IOException {
        this.fileName = fileName;
        File file = new File(this.fileName);
        if (!file.exists()){
            file.createNewFile();
        }
    }
    @Override
    public void print(String data) throws IOException {
        FileWriter writer = new FileWriter(this.fileName);
        writer.append(data);
        writer.close();
    }
}
