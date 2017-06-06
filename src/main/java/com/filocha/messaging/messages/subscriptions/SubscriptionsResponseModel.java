package com.filocha.messaging.messages.subscriptions;

import java.util.List;

public class SubscriptionsResponseModel {

    private List<String> userSubscriptions;

    public List<String> getUserSubscriptions() {
        return userSubscriptions;
    }

    public void setUserSubscriptions(List<String> userSubscriptions) {
        this.userSubscriptions = userSubscriptions;
    }


}
