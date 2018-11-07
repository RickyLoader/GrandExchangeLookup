/**
 * ExchangeData.java - Reads in a JSON summary of all items and their attributes, creates a searchable data set.
 *
 * @author Ricky Loader
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ExchangeData {

    /* Input stream for the JSON URL. */
    private static InputStream inputStream;

    /* Input reader for the input stream. */
    private static InputStreamReader inputReader;

    /* BufferedReader to iterate through InputStreamReader. */
    private static BufferedReader inputParser;

    /* URL of the JSON summary. */
    private static String exchangeURL = "https://rsbuddy.com/exchange/summary.json";

    /* HashMap to store Item objects by their unique ID. */
    private static HashMap<Long, Item> items;

    /* HashMap to store item names by their unique ID (item names are not unique). */
    private static HashMap<Long, String> itemNames;

    /**
     * Initialise the maps with Item objects created from the JSON summary.
     */
    public ExchangeData() {
        fetchItems();
    }

    /**
     * Read in JSON summary to create Item objects and store in maps to be queried.
     */
    private static void fetchItems() {
        try {
            /* Read in the JSON summary data. */
            inputStream = new URL(exchangeURL).openStream();
            inputReader = new InputStreamReader(inputStream);
            inputParser = new BufferedReader(inputReader);

            /* JSON objects are separated by the specified pattern. */
            String excess = "\\{?,?\"[0-9]+\":";

            String[] rawItemData;

            /* Splitting on pattern results in an array of unique JSON item objects. */
            rawItemData = inputParser.readLine().split(excess);

            items = new HashMap<>();
            itemNames = new HashMap<>();

            /* Iterate over JSON objects to create Item objects. */
            for (String item : rawItemData) {
                if (item.length() > 0) {

                    /* Remove excess formatting and Unicode characters. */
                    String itemJson = item.
                            replace("{", "")
                            .replace("}", "")
                            .replace("\"", "")
                            .replace("\\u0027", "");

                    /* Split each JSON object by comma separator, leaving array of attribute:value pairs. */
                    String[] attrValue = itemJson.split(",");

                    /* Create an Item object from the JSON object's attribute:value pairs. */
                    Item newItem = createItem(attrValue);

                    /* Store the Item object by the unique ID & name. */
                    items.put(newItem.getItemID(), newItem);
                    itemNames.put(newItem.getItemID(), newItem.getItemName().toLowerCase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes a JSON object's attribute:value pairs and creates an Item object.
     *
     * @param item A String array of attribute:value pairs.
     * @return A new Item object from the JSON object's attribute values.
     */
    private static Item createItem(String[] item) {
        long id = 0;
        String name = "";
        long sellPrice = 0;
        long buyPrice = 0;
        long avgPrice = 0;

        /* Iterate over attribute:value pairs. */
        for (String attributeValuePairs : item) {

            /* Split on colon to obtain attribute and value in separate Strings. */
            String attribute = attributeValuePairs.split(":")[0];
            String value = attributeValuePairs.split(":")[1];

            /* Obtain the required values for an Item object. */
            if (attribute.equals("id")) {
                id = Long.valueOf(value);
            }
            else if (attribute.equals("name")) {
                name = value;
            }
            else if (attribute.equals("sell_average")) {
                sellPrice = Long.valueOf(value);
            }
            else if (attribute.equals("buy_average")) {
                buyPrice = Long.valueOf(value);
            }
            else if (attribute.equals("overall_average")) {
                avgPrice = Long.valueOf(value);
            }
        }

        /* Return the Item object created from the JSON object. */
        return new Item(id, name, sellPrice, buyPrice, avgPrice);
    }

    /**
     * Takes a String identifier from the user and attempts to locate the Item object.
     *
     * @param identifier A String from the user, representing either an item ID or item name.
     * @return A String summary of the requested item or an error message.
     */
    public static String requestItem(String identifier) {

        /* Attempt to locate the key for the given item, and find the item it represents. */
        Item i = getItem(getItemKey(identifier));

        /* If the identifier does not represent an item, return an error message. */
        if (i == null) {
            return "Item " + identifier + " does not exist, try again!\n\n";
        }

        /* Otherwise generate a summary of the item. */
        return Summary(i);
    }

    /**
     * Processes the user input to determine if it is an item ID or item name
     * and attempts to locate the associated map key.
     *
     * @param identifier A String from the user, either an item ID or item name.
     * @return A key to the item map to obtain the desired Item object.
     */
    private static Long getItemKey(String identifier) {

        /* Initialise key to null (non existent). */
        Long key = null;

        /* If the input is a Long, it is an item ID (item names are never entirely digits). */
        if (isLong(identifier) && items.containsKey(Long.valueOf(identifier))) {

            /* Parse the String to a Long to obtain the key (item ID). */
            key = Long.valueOf(identifier);
        }

        /* If the item name is found in the ID to name map. */
        else if (itemNames.containsValue(identifier)) {

            /* Iterate over the stored values (item names) in the map. */
            for (Map.Entry e : itemNames.entrySet()) {

                /* If the given item name is found. */
                if (e.getValue().equals(identifier)) {

                    /* Obtain the key (item ID) of the item name. */
                    key = (Long) e.getKey();
                }
            }
        }

        /* Return the key (item ID) if it is found, or null. */
        return key;
    }

    /**
     * Returns the Item object stored at the desired key, or null if it does not exist.
     *
     * @param key An item ID used to obtain the associated Item object.
     * @return The Item object associated with the key, or null if the item does not exist.
     */
    private static Item getItem(Long key) {
        if (key != null) {
            return items.get(key);
        }
        return null;
    }

    /**
     * Takes the user input and checks if it is a Long. If so, the user is searching by item ID.
     * If not, the user is searching by item name.
     *
     * @param query The user input.
     * @return The result of parsing the user input for a Long.
     */
    private static boolean isLong(String query) {
        Boolean result = false;
        try {
            Long.parseLong(query);
            result = true;
        } catch (NumberFormatException e) {
        }
        return result;
    }

    /**
     * Generates a summary output from a given Item object.
     *
     * @param i An Item object to generate the summary from.
     * @return A String containing the Item object's summary.
     */
    private static String Summary(Item i) {
        String result = "\n\n";
        result +=
                i.getItemName() + " (ITEM ID: " + i.getItemID() + ")\n" +
                        "\nBuy price: " + i.getItemBuyPrice() + "\n" +
                        "Sell price: " + i.getItemSellPrice() + "\n" +
                        "Average price: " + i.getItemAveragePrice() + "\n\n";
        return (result);
    }
}