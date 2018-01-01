package com.filocha.messaging.messages.subscriptions;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SubscriptionsRequestModel {
    private String email;
}
