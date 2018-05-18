package com.youhaoxi.livelink.gateway.dispatch.mq;

import com.rabbitmq.client.*;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.common.util.NetUtils;
import com.youhaoxi.livelink.gateway.dispatch.mq.downstream.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RabbitConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitConsumer.class);

    private static final String EXCHANGE_NAME = Constants.EXCHANGE_NAME;
    private static Channel channel = RabbitConnectionManager.getInstance().getNewChannel();
    private String queueName;

    private Sender sender;


    public RabbitConsumer(){
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            queueName = channel.queueDeclare().getQueue();

        } catch (IOException e) {
            logger.error("RabbitProducer error:"+e.getMessage(),e);
        }
    }

    /**
     * routeKey就是本机hostip
     * @param routekey
     */
    public  void consume(String routekey){
        try {

            channel.queueBind(queueName, EXCHANGE_NAME, routekey);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    logger.debug(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");

                    //下发给用户
                    sender.send(body);

                }
            };
            channel.basicConsume(queueName, true, consumer);

        } catch (IOException e) {
            logger.error("publish error:"+e.getMessage(),e);
        }
    }



    public static void main(String[] argv) throws Exception {
        String host = NetUtils.getLocalAddress().getHostAddress();
        new RabbitConsumer().consume(host);
    }

    public Sender getSender() {
        return sender;
    }

    public RabbitConsumer setSender(Sender sender) {
        this.sender = sender;
        return this;
    }
}
