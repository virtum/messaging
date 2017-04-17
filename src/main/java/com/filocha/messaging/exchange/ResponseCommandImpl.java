package com.filocha.messaging.exchange;

import com.google.gson.Gson;

public class ResponseCommandImpl<TRequest, TResponse> implements ResponseCommand {
    private RequestHandler<TRequest, TResponse> handler;
    private Gson gson = new Gson();
    private Class<TRequest> reqClazz;
    private Class<TResponse> respClass;

    public ResponseCommandImpl(RequestHandler<TRequest, TResponse> handler, Class<TRequest> reqClazz,
                               Class<TResponse> respClazz) {
        this.handler = handler;
        this.reqClazz = reqClazz;
        this.respClass = respClazz;
    }

    public boolean support(Class<?> reqClass) {
        return reqClass.equals(reqClazz);
    }

    public String onMessage(String message) {
        TRequest pojo = gson.fromJson(message, reqClazz);
        TResponse response = handler.onMessage(pojo);

        return gson.toJson(response);
    }

    public Class<?> getResponseType() {
        return respClass;
    }
}