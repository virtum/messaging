package com.filocha.messaging.server;

public interface ServerBus {
    <TRequest, TResponse> void addHandler(RequestHandler<TRequest, TResponse> handler, Class<TRequest> reqClazz,
                                          Class<TResponse> clazz);
}
