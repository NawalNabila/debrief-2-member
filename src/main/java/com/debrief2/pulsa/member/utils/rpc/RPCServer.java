package com.debrief2.pulsa.member.utils.rpc;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.model.OTP;
import com.debrief2.pulsa.member.model.User;
import com.debrief2.pulsa.member.payload.request.BalanceRequest;
import com.debrief2.pulsa.member.payload.request.UserRequest;
import com.debrief2.pulsa.member.payload.request.VerifyPinRequest;
import com.debrief2.pulsa.member.payload.response.UserResponse;
import com.debrief2.pulsa.member.service.OTPService;
import com.debrief2.pulsa.member.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Component
public class RPCServer {
    @Autowired
    private UserService userService;

    @Autowired
    private OTPService otpService;

    private static final Logger log = LoggerFactory.getLogger(RpcServer.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${cloudAMQP.url}")
    private String url;

    @Async("workerExecutor")
    public void run(String queueName) throws URISyntaxException {
        final URI rabbitMqUrl = new URI(url);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(rabbitMqUrl.getUserInfo().split(":")[0]);
        factory.setPassword(rabbitMqUrl.getUserInfo().split(":")[1]);
        factory.setHost(rabbitMqUrl.getHost());
        factory.setPort(rabbitMqUrl.getPort());
        factory.setVirtualHost(rabbitMqUrl.getPath().substring(1));

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queuePurge(queueName);
            channel.basicQos(1);
            log.info("["+queueName+"] Awaiting requests");

            Object monitor = new Object();
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                String response = "";
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info("["+queueName+"] receiving request for "+message);

                try {
                    switch (queueName){
                        case "register":
                            UserRequest request = objectMapper.readValue(message, UserRequest.class);
                            UserResponse registerResponse = userService.register(request);
                            response = objectMapper.writeValueAsString(registerResponse);
                            break;
                        case "login":
                            UserResponse loginResponse = userService.login(message);
                            response = objectMapper.writeValueAsString(loginResponse);
                            break;
                        case "verifyPin":
                            VerifyPinRequest pinRequest = objectMapper.readValue(message, VerifyPinRequest.class);
                            UserResponse verifyPinResponse = userService.verifyPin(pinRequest.getId(), pinRequest.getPin());
                            response = objectMapper.writeValueAsString(verifyPinResponse);
                            break;
                        case "getProfile":
                            UserResponse profileResponse = userService.getProfile(Long.parseLong(message));
                            response = objectMapper.writeValueAsString(profileResponse);
                            break;
                        case "getBalance":
                            long balanceResponse = userService.getBalance(Long.parseLong(message));
                            response = objectMapper.writeValueAsString(Long.toString(balanceResponse));
                            break;
                        case "decreaseBalance":
                            BalanceRequest decreaseRequest = objectMapper.readValue(message, BalanceRequest.class);
                            userService.decreaseBalance(decreaseRequest.getId(), decreaseRequest.getValue());
                            response = objectMapper.writeValueAsString("success");
                            break;
                        case "increaseBalance":
                            BalanceRequest increaseRequest = objectMapper.readValue(message, BalanceRequest.class);
                            userService.increaseBalance(increaseRequest.getId(), increaseRequest.getValue());
                            response = objectMapper.writeValueAsString("success");
                            break;
                        case "sendOTP":
                            OTP otpResponse = otpService.sendOTP(Long.parseLong(message));
                            response = objectMapper.writeValueAsString(otpResponse);
                            break;
                        default:
                            response = "Unknown service method";
                            break;
                    }
                } catch (ServiceException serviceException) {
                    response = serviceException.getMessage();
                }

                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes(StandardCharsets.UTF_8));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                synchronized (monitor) {
                    monitor.notify();
                }
            };

            channel.basicConsume(queueName, false, deliverCallback, (consumerTag -> { }));
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
