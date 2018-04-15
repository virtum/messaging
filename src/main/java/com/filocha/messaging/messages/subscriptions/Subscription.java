package com.filocha.messaging.messages.subscriptions;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Subscription {

    private String itemName;
    private String creationDate;
    private Integer numberOfFoundItems;
}
