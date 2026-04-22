package com.pawport;

public class WishlistItem {
    private int itemId;
    private String itemName;
    private String itemType;
    private String location;

    public WishlistItem(int itemId, String itemName, String itemType, String location) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.location = location;
    }

    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getItemType() { return itemType; }
    public String getLocation() { return location; }
}