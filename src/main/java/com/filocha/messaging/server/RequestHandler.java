package com.filocha.messaging.server;

public interface RequestHandler<TRequest, TResponse> {

    TResponse onMessage(TRequest request);

    Class<TRequest> getRequestClass();

    Class<TResponse> getResponseClass();
}
