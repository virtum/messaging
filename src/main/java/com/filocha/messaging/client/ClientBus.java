package com.filocha.messaging.client;

import java.util.concurrent.CompletableFuture;

public interface ClientBus {

    <TRequest, TResponse> CompletableFuture<TResponse> sendRequest(TRequest request, Class<TRequest> reqClazz);
}
