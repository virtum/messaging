package com.filocha.messaging.messages.finder;

public class ItemFinderRequestMessage {
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    private String itemName;
}
