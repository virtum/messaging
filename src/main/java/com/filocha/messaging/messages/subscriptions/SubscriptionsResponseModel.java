package com.filocha.messaging.messages.subscriptions;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class SubscriptionsResponseModel {

    private List<Subscription> userSubscriptions;
    private Long subscriptionCounter;
}
