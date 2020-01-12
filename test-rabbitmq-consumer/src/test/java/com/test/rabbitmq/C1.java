package com.test.rabbitmq;

import com.rabbitmq.client.*;

public class C1 {
    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    //交换机名称
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;

        try {
            //创建一个与MQ的连接
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            //创建一个tcp连接
            connection = factory.newConnection();
            //通过连接创建一个通道，每个通道代表一个会话
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);

            //绑定email通知队列
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_TOPICS_INFORM, "inform.#.email.#");

            //消费消息
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received Email:'" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            channel.basicConsume(QUEUE_INFORM_EMAIL, true, deliverCallback, consumerTag -> {
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
