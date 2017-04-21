package com.filocha.messaging.server;

public interface ResponseCommand {
    boolean support(Class<?> reqClass);

    String onMessage(String message);

    Class<?> getResponseType();

}
