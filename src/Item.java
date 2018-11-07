/**
 * Item.java - An object representing an item from the game, stores the item's name, price information, and ID.
 *
 * @author Ricky Loader
 * @version 1.0
 */

import java.text.DecimalFormat;

public class Item {

    private String itemName;
    private long itemSellPrice;
    private long itemBuyPrice;
    private long itemAveragePrice;
    private long itemID;

    /* Readable format for the output price. */
    DecimalFormat df = new DecimalFormat("#,###GP");

    /**
     * Constructor for the Item object, takes parameters from the item's JSON object.
     *
     * @param itemID           Unique ID of the item.
     * @param itemName         Name of the item.
     * @param itemSellPrice    Sell price of the item.
     * @param itemBuyPrice     Buy price of the item.
     * @param itemAveragePrice Average price of the item (JSON attribute, not calculated).
     */
    public Item(long itemID, String itemName, long itemSellPrice, long itemBuyPrice, long itemAveragePrice) {
        this.itemName = itemName;
        this.itemID = itemID;
        this.itemAveragePrice = itemAveragePrice;
        this.itemSellPrice = itemSellPrice;
        this.itemBuyPrice = itemBuyPrice;
    }

    /**
     * @return Name of the item.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @return Unique ID of the item.
     */
    public long getItemID() {
        return itemID;
    }

    /**
     * @return Sell price of the item.
     */
    public String getItemSellPrice() {
        return format(itemSellPrice);
    }

    /**
     * @return Buy price of the item.
     */
    public String getItemBuyPrice() {
        return format(itemBuyPrice);
    }

    /**
     * @return Average traded price of the item.
     */
    public String getItemAveragePrice() {
        return format(itemAveragePrice);
    }

    /**
     * Formats a given price in to a readable String for output.
     *
     * @param price Price to be formatted.
     * @return A formatted String of the given price.
     */
    private String format(long price) {
        /* Prices below 1000 don't require comma separation, only a currency suffix. */
        if (price >= 1000) {
            return df.format(price);
        } else {
            return String.valueOf(price) + "GP";
        }
    }
}