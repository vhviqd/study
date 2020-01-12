package com.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class P {
    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    //交换机名称
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        //创建连接工厂
        try {
            //创建一个与MQ的连接
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            //创建一个tcp连接
            connection = factory.newConnection();
            //通过连接创建一个通道，每个通道代表一个会话
            channel = connection.createChannel();
            /**
             * 声明交换机：参数如下
             * 1、交换机名称
             * 2、交换机类型，fanout、topic、direct、headers
             */
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
            /**
             * 声明队列：参数如下：
             * 1、队列名称
             * 2、是否持久化
             * 3、是否独占此队列
             * 4、队列不用是否自动删除
             * 5、参数
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);

            //发送邮件Email消息
            /*for (int i = 0; i < 10; i++) {
                String message = "email inform to user : " + i;
                *//**
                 * 向交换机发送消息  :参数明细
                 * 1、交换机名称，不指令使用默认交换机名称 Default Exchange
                 * 2、routingKey（路由key），根据key名称将消息转发到具体的队列，这里填写队列名称表示消息将发到此队列
                 * 3、消息属性
                 * 4、消息内容
                 *//*
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.email", null, message.getBytes());
                System.out.println("Send Message is:'" + message + "'");
            }*/
            //发送短信消息
           /* for (int i = 0; i < 10; i++) {
                String message = "sms inform to user : " + i;
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.sms", null, message.getBytes());
                System.out.println("Send Message is:'" + message + "'");
            }*/
            //发送短信和邮件消息
            for (int i = 0; i < 10; i++) {
                String message = "sms and email inform to user" + i;
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.sms.email", null, message.getBytes());
                System.out.println("Send Message is:'" + message + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
