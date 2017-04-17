package com.filocha.messaging.exchange;

public interface ResponseCommand {
    boolean support(Class<?> reqClass);

    String onMessage(String message);

    Class<?> getResponseType();
}
