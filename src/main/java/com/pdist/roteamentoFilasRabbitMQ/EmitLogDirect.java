package com.pdist.roteamentoFilasRabbitMQ;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {

    private static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        HashMap<String,String> options = new HashMap<>();
        options.put("1","biscoito");
        options.put("2","bolacha");

        ConnectionFactory connnectionFactory = new ConnectionFactory();
        connnectionFactory.setHost("localhost");

        connnectionFactory.setUsername("guest");
        connnectionFactory.setPassword("guest");

        System.out.println("Aluno: Luciano de Carvalho Souza Filho");
        String option;

        while(true) {
            System.out.print("Deseja enviar a mensagem para biscoito ou balacha? (1-Biscoito | 2-Bolacha): ");
            option = scan.nextLine();

            if(option != null && !option.equals("") && options.containsKey(option)) break;
            else System.out.print("Opção invalida\n");
        }


        System.out.print("Digite a frase a ser enviada: ");
        scan = new Scanner(System.in);
        String text = scan.nextLine();
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        String value = options.get(option);

        try (Connection con = connnectionFactory.newConnection();
             Channel channel = con.createChannel();) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            channel.basicPublish(EXCHANGE_NAME, value, null, textBytes);
            System.out.println(String.format("Enviando mensagem '%s' para '%s'.\n",text, value));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}