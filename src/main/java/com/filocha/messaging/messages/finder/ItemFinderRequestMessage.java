package com.filocha.messaging.messages.finder;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ItemFinderRequestMessage {

    private String email;
    private String item;
}
