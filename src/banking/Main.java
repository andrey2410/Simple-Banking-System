package banking;
import banking.ui.UI;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        UI ui=new UI();
        ui.start(scanner);

    }
}
