/**
 * SearchGrandExchange.java - Search OSBuddy's Grand Exchange JSON summary for an item and receive the information.
 *
 * @author Ricky Loader
 * @version 1.0
 */

import java.util.Scanner;

public class SearchGrandExchange {

    /**
     * Take user input in the form of a String and return the item's information if it exists.
     *
     * @param args Command line input.
     */
    public static void main(String[] args) {

        /* Scanner to take user input. */
        Scanner scan = new Scanner(System.in);

        System.out.println("\nFetching Grand Exchange data...\n\n");

        /* Initialise the Grand Exchange data. */
        ExchangeData exchange = new ExchangeData();

        System.out.println("\nPlease enter an item name or item id:\n\n");

        /*
         * Read in user input and print the result of searching for the given item,
         * either prints the item information or a message informing the user that the item doesn't exist.
         */
        while (scan.hasNextLine()) {
            String line = scan.nextLine().toLowerCase();
            System.out.println(exchange.requestItem(line));
        }
    }
}