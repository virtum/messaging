package com.filocha.messaging.messages.subscriptions;

import lombok.Builder;

import java.util.List;

@Builder
public class SubscriptionsResponseModel {

    private List<String> userSubscriptions;
}
