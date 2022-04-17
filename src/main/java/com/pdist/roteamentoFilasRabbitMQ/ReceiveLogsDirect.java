package com.pdist.roteamentoFilasRabbitMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws IOException, TimeoutException {

        ConnectionFactory connnectionFactory = new ConnectionFactory();

        connnectionFactory.setHost("localhost");
        connnectionFactory.setUsername("guest");
        connnectionFactory.setPassword("guest");

        Connection con = connnectionFactory.newConnection();
        Channel channel = con.createChannel();

        System.out.println("Aluno: Luciano de Carvalho Souza Filho");

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "biscoito");
        channel.queueBind(queueName, EXCHANGE_NAME, "bolacha");

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String text = new String(delivery.getBody(), "UTF-8");
            System.out.println(String.format("[x] Received '%s': '%s'", delivery.getEnvelope().getRoutingKey(), text));
        };

        channel.basicConsume(queueName, true, callback, consumerTag -> {});
    }
}