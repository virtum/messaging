package com.filocha.messaging.messages.subscriptionDetails;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UrlDetails {

    private String url;
    private String dateOfFinding;
}
