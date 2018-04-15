package com.filocha.messaging.messages.subscriptionDetails;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class SubscriptionDetailsResponseModel {

    private String itemName;
    private List<String> urls;
}
