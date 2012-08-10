package com.rabbitmq.jms.admin;

import java.io.Serializable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.jms.client.RMQSession;

public class RMQDestination implements Queue, Topic, Destination, Referenceable, Serializable {

    /** TODO */
    private static final long serialVersionUID = 596966152753718825L;
    private volatile String name;
    private volatile String exchangeName;
    private volatile String routingKey;
    private volatile boolean queue;
    private volatile boolean declared;

    public RMQDestination() {
    }

    /**
     * Creates a destination, either a queue or a topic.
     * 
     * @param name - the name of the topic or the queue
     * @param exchangeName - the exchange we will publish to and bind queues to
     * @param routingKey - the routing key used for this destination
     * @param queue - true if this is a queue, false if this is a topic
     * @param declared - true if we have called
     *            {@link Channel#queueDeclare(String, boolean, boolean, boolean, java.util.Map)}
     *            or
     *            {@link Channel#exchangeDeclare(String, String, boolean, boolean, boolean, java.util.Map)
     *            to represent this queue/topic in the RabbitMQ broker. If creating a topic/queue to
     *            bind in JNDI, this value must be set to FALSE
     */
    public RMQDestination(String name, String exchangeName, String routingKey, boolean queue, boolean declared) {
        this.name = name;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.queue = queue;
        this.declared = declared;
    }

    /**
     * Returns the name of the queue/topic
     * @return the name of the queue/topic
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the name of the RabbitMQ Exchange used to publish/send messages to
     * @return the name of the RabbitMQ Exchange used to publish/send messages to
     */
    public String getExchangeName() {
        return this.exchangeName;
    }

    
    /**
     * Returns the routingKey used to publish/send messages with
     * @return the routingKey used to publish/send messages with
     */
    public String getRoutingKey() {
        return this.routingKey;
    }

    /**
     * Returns true if this is a queue, false if it is a topic
     * @return true if this is a queue, false if it is a topic
     */
    public boolean isQueue() {
        return this.queue;
    }

    /**
     * Sets the name of the queue/topic - should only be used when binding into JNDI
     * @param name
     * @throws IllegalStateException if the queue has already been declared {@link RMQDestination#isDeclared()} return true
     */
    public void setName(String name) {
        if (isDeclared()) throw new IllegalStateException();
        this.name = name;
    }

    /**
     * Sets the name of the exchange in the RabbitMQ broker - should only be used when binding into JNDI
     * @param exchangeName
     * @throws IllegalStateException if the queue has already been declared {@link RMQDestination#isDeclared()} return true
     */
    public void setExchangeName(String exchangeName) {
        if (isDeclared()) throw new IllegalStateException();
        this.exchangeName = exchangeName;
    }

    /**
     * Sets the routing key when sending/receiving messages for this queue/topic - should only be used when binding into JNDI
     * @param routingKey
     * @throws IllegalStateException if the queue has already been declared {@link RMQDestination#isDeclared()} return true
     */
    public void setRoutingKey(String routingKey) {
        if (isDeclared()) throw new IllegalStateException();
        this.routingKey = routingKey;
    }

    /**
     * Set to true if this is a queue, false if this is a topic - should only be used when binding into JNDI
     * @param queue
     * @throws IllegalStateException if the queue has already been declared {@link RMQDestination#isDeclared()} return true
     */
    public void setQueue(boolean queue) {
        if (isDeclared()) throw new IllegalStateException();
        this.queue = queue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTopicName() throws JMSException {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getQueueName() throws JMSException {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reference getReference() throws NamingException {
        return new Reference(this.getClass().getCanonicalName());
    }


    /**
     * 
     * @return true if we have called
     *            {@link Channel#queueDeclare(String, boolean, boolean, boolean, java.util.Map)}
     *            or
     *            {@link Channel#exchangeDeclare(String, String, boolean, boolean, boolean, java.util.Map)
     *            to represent this queue/topic in the RabbitMQ broker. If creating a topic/queue to
     *            bind in JNDI, this value will be set to false until the queue/topic has been
     *            setup in the RabbitMQ broker
     */
    public boolean isDeclared() {
        return declared;
    }

    /**
     * Should only be used internally by {@link RMQSession}
     * @param declared - set to true if the queue/topic has been defined in the RabbitMQ broker
     * @throws IllegalStateException if the queue has already been declared {@link RMQDestination#isDeclared()} return true
     * @see {@link #isDeclared()}
     */
    public void setDeclared(boolean declared) {
        this.declared = declared;
    }

}
