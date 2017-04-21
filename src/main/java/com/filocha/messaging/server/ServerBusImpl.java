package com.filocha.messaging.server;

import com.filocha.messaging.connection.ActiveMQSessionFactory;
import com.google.gson.Gson;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ServerBusImpl implements ServerBus, MessageListener, AutoCloseable {

    private List<ResponseCommand> handlers = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(ServerBusImpl.class);
    private MessageConsumer consumer;
    private MessageProducer producer;


    public void setConsumerAndProducer(String activeMqPort) {
        try {
            ActiveMQSessionFactory activeMQSessionFactory = new ActiveMQSessionFactory();
            Session session = activeMQSessionFactory.createConnection(activeMqPort);

            consumer = session.createConsumer(session.createQueue("REQUEST.QUEUE"));

            MessageListener listener = this;
            consumer.setMessageListener(listener);

            producer = session.createProducer(session.createQueue("RESPONSE.QUEUE"));
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        } catch (JMSException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
            String corrId = bytesMessage.getCorrelationId();

            String classType = bytesMessage.getType();
            Class<?> reqClazz = Class.forName(classType);

            ActiveMQBytesMessage response = new ActiveMQBytesMessage();
            String msg = new String(bytesMessage.getContent().data, "UTF-8");

            for (ResponseCommand handler : handlers) {
                if (!handler.support(reqClazz)) {
                    continue;
                }
                String res = handler.onMessage(msg);

                response.setType(handler.getResponseType().getName());
                response.setCorrelationId(corrId);
                response.writeBytes(res.getBytes());
                producer.send(response);
            }
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        } catch (JMSException e) {
            logger.error(e.getMessage());
        }
    }

    public <TRequest, TResponse> void addHandler(RequestHandler<TRequest, TResponse> handler, Class<TRequest> reqClazz,
                                                 Class<TResponse> respClazz) {
        ResponseCommand command = new ResponseCommandImpl<>(handler, reqClazz, respClazz);
        handlers.add(command);
    }

    private static final class ResponseCommandImpl<TRequest, TResponse> implements ResponseCommand {
        private RequestHandler<TRequest, TResponse> handler;
        private Gson gson = new Gson();
        private Class<TRequest> reqClazz;
        private Class<TResponse> respClass;

        ResponseCommandImpl(RequestHandler<TRequest, TResponse> handler, Class<TRequest> reqClazz,
                            Class<TResponse> respClazz) {
            this.handler = handler;
            this.reqClazz = reqClazz;
            this.respClass = respClazz;
        }

        @Override
        public boolean support(Class<?> reqClass) {
            return reqClass.equals(reqClazz);
        }

        @Override
        public String onMessage(String message) {
            TRequest pojo = gson.fromJson(message, reqClazz);
            TResponse response = handler.onMessage(pojo);

            return gson.toJson(response);
        }

        @Override
        public Class<?> getResponseType() {
            return respClass;
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
