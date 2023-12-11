package IO;
import static ConsoleApp.App.scanner;

public class ConsoleIO implements IO{
    @Override
    public String readData() {
        System.out.print("please enter the data: ");
        scanner.nextLine();
        return scanner.nextLine();
    }

    @Override
    public void print(String data) {
        System.out.println(data);
    }
}
