package com.filocha.messaging.messages.finder;

public class ItemFinderRequestMessage {


    private String email;
    private String item;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }


}
