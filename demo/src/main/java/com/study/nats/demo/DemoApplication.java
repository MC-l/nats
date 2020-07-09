package com.study.nats.demo;

import io.nats.client.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        //SpringApplication.run(DemoApplication.class, args);
        Connection nc = Nats.connect("nats://120.238.78.213:4222");
        String subject = "meeting";
        publish(nc,subject);
        //subscribe(nc,subject);
    }

    private static void subscribe(Connection nc,String subject) throws IOException, InterruptedException {
//        Subscription sub = nc.subscribe(subject);
//        Message msg = sub.nextMessage(Duration.ofMillis(500));
//        System.out.println(msg);
//        //String response = new String(msg.getData(), StandardCharsets.UTF_8);

        Dispatcher d = nc.createDispatcher((msg) -> {
            String response = new String(msg.getData(), StandardCharsets.UTF_8);
            System.out.println("收到消息："+response);
        });

        d.subscribe(subject);
    }

    private static void publish(Connection nc,String subject) throws InterruptedException {
        String msg = "{\"request\":true,\"id\":1628871,\"reply\":\"\",\"method\":\"room-empty\",\"data\":{\"rid\":\"199808315\"}}";
        new Thread(()->{
            for (int i = 0; i < 1; i++) {
                nc.publish(subject, msg.getBytes(StandardCharsets.UTF_8));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
