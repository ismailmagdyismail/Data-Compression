package Printer;

public class ConsolePrinter implements IPrinter {
    @Override
    public void print(String data) {
        System.out.println(data);
    }
}
