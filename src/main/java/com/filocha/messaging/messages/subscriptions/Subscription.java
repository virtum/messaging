package com.filocha.messaging.messages.subscriptions;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Builder
@Value
public class Subscription {

    private String itemName;
    private String creationDate;
    private Integer numberOfFoundItems;
}
