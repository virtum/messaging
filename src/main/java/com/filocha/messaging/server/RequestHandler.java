package com.filocha.messaging.server;

public interface RequestHandler<TRequest, TResponse> {

    TResponse onMessage(TRequest request);
}
