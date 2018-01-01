package com.filocha.messaging.client;

import com.filocha.messaging.connection.ActiveMQSessionFactory;
import com.google.gson.Gson;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ClientBusImpl implements ClientBus, MessageListener, AutoCloseable {

    private ConcurrentHashMap<String, CompletableFuture> futures = new ConcurrentHashMap<>();
    private Gson gson = new Gson();
    private static Logger logger = Logger.getLogger(ClientBusImpl.class);

    private MessageProducer producer;
    private MessageConsumer consumer;

    public void setConsumerAndProducer(String activeMqPort, String consumerQueue, String producerQueue) {
        try {
            ActiveMQSessionFactory activeMQSessionFactory = new ActiveMQSessionFactory();
            Session session = activeMQSessionFactory.createConnection(activeMqPort);

            consumer = session.createConsumer(session.createQueue(consumerQueue));

            MessageListener listener = this;
            consumer.setMessageListener(listener);

            producer = session.createProducer(session.createQueue(producerQueue));
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public <TRequest, TResponse> CompletableFuture<TResponse> sendRequest(TRequest request, Class<TRequest> reqClazz) {
        final CompletableFuture<TResponse> result = new CompletableFuture<>();

        String toSend = gson.toJson(request);

        ActiveMQBytesMessage req = new ActiveMQBytesMessage();
        String corrId = UUID.randomUUID().toString();

        req.setType(reqClazz.getName());
        req.setCorrelationId(corrId);
        try {
            req.writeBytes(toSend.getBytes());
            futures.putIfAbsent(corrId, result);
            producer.send(req);
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public void onMessage(Message message) {
        try {
            ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
            String msg = new String(bytesMessage.getContent().data, "UTF-8");

            String classType = bytesMessage.getType();
            Class<?> resClazz = Class.forName(classType);

            for (String key : futures.keySet()) {
                if (bytesMessage.getCorrelationId().equals(key)) {
                    Object object = gson.fromJson(msg, resClazz);

                    futures.remove(key).complete(object);
                }
            }

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

    public void close() {
        try {
            producer.close();
            consumer.close();
        } catch (JMSException e) {
            logger.error(e.getMessage());
        }
    }
}
