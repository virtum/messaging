package com.filocha.messaging.connection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;

public class ActiveMQSessionFactory implements ExceptionListener {

    private Connection connection;
    private Session session;
    private Logger logger = LoggerFactory.getLogger(ActiveMQSessionFactory.class);

    public void createConnection(String activeMqPort) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(activeMqPort);
            connection = connectionFactory.createConnection();
            connection.start();

            connection.setExceptionListener(this);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        } catch (JMSException e) {
            logger.error(e.getMessage());
        }
    }

    public synchronized void onException(JMSException ex) {
        logger.error("JMS Exception occured.  Shutting down client.");
    }

    public void close() {
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            logger.error(e.getMessage());
        }
    }

}
