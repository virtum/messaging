package com.filocha.messaging.server;

import com.filocha.messaging.exchange.RequestHandler;

public interface ServerBus {
    <TRequest, TResponse> void addHandler(RequestHandler<TRequest, TResponse> handler, Class<TRequest> reqClazz,
                                          Class<TResponse> clazz);
}
