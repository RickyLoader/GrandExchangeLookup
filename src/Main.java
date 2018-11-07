import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nFetching Grand Exchange data...\n\n");
        ExchangeData exchange = new ExchangeData();
        System.out.println("\nPlease enter an item name or item id:\n\n");
        while (scan.hasNextLine()) {
            //String line = scan.nextLine().toLowerCase();
            System.out.println(exchange.requestItem(line));
        }
    }
}


