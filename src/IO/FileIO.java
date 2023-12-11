package IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileIO implements IO{
    protected String readFilePath, writeFilePath;

    public FileIO(String readFilePath, String writeFilePath) {
        this.readFilePath = readFilePath;
        this.writeFilePath = writeFilePath;
    }

    @Override
    public String readData() {
        StringBuilder content = new StringBuilder();
        try {
            File file = new File(readFilePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                content.append(line);
                if(scanner.hasNextLine()) {
                    content.append("\n");
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Override
    public void print(String data) {
        try {
            FileWriter writer = new FileWriter(this.writeFilePath);
            writer.write(data);
            writer.close();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
