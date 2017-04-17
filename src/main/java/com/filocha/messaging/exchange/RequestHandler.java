package com.filocha.messaging.exchange;

public interface RequestHandler<TRequest, TResponse> {

    TResponse onMessage(TRequest request);
}
