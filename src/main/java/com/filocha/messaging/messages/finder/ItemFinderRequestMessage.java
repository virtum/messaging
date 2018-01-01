package com.filocha.messaging.messages.finder;

import lombok.Builder;

@Builder
public class ItemFinderRequestMessage {

    private String email;
    private String item;
}
