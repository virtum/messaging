package com.filocha.messaging.messages.subscriptionDetails;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SubscriptionDetailsRequestModel {

    private String email;
    private String itemName;
}
