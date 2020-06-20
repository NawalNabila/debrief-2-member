package com.debrief2.pulsa.member.utils.rpc;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.model.OTP;
import com.debrief2.pulsa.member.model.User;
import com.debrief2.pulsa.member.payload.request.BalanceRequest;
import com.debrief2.pulsa.member.payload.request.OTPRequest;
import com.debrief2.pulsa.member.payload.request.UserRequest;
import com.debrief2.pulsa.member.payload.request.VerifyPinRequest;
import com.debrief2.pulsa.member.payload.response.OTPResponse;
import com.debrief2.pulsa.member.payload.response.UserResponse;
import com.debrief2.pulsa.member.service.OTPService;
import com.debrief2.pulsa.member.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

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
                            String decrease = userService.decreaseBalance(decreaseRequest.getId(), decreaseRequest.getValue());
                            response = objectMapper.writeValueAsString(decrease);
                            break;
                        case "sendOTP":
                            OTPResponse otpResponse = otpService.sendOTP(Long.parseLong(message));
                            response = objectMapper.writeValueAsString(otpResponse);
                            break;
                        case "getOTP":
                            OTPResponse getOTPResponse = otpService.getOTP(Long.parseLong(message));
                            response = objectMapper.writeValueAsString(getOTPResponse);
                            break;
                        case "verifyOTP":
                            OTPRequest otpRequest = objectMapper.readValue(message, OTPRequest.class);
                            OTPResponse verifyOTPResponse = otpService.verifyOTP(otpRequest.getId(), otpRequest.getCode());
                            response = objectMapper.writeValueAsString(verifyOTPResponse);
                            break;
                        case "changePin":
                            VerifyPinRequest pin = objectMapper.readValue(message, VerifyPinRequest.class);
                            String changePinResponse = userService.changePin(pin.getId(), pin.getPin());
                            response = objectMapper.writeValueAsString(changePinResponse);
                            break;
                        default:
                            response = "Unknown service method";
                            break;
                    }
                } catch (ServiceException | NullPointerException exception) {
                    response = exception.getMessage();
                } catch (InvalidFormatException | NumberFormatException e) {
                    response = "invalid request format";
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

    @Async("workerExecutor")
    public void runPersistent(String queueName) throws URISyntaxException, IOException, TimeoutException {
        final URI rabbitMqUrl = new URI(url);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(rabbitMqUrl.getUserInfo().split(":")[0]);
        factory.setPassword(rabbitMqUrl.getUserInfo().split(":")[1]);
        factory.setHost(rabbitMqUrl.getHost());
        factory.setPort(rabbitMqUrl.getPort());
        factory.setVirtualHost(rabbitMqUrl.getPath().substring(1));

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, true, false, false, null);
        log.info("["+queueName+"] Awaiting requests");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            log.info("["+queueName+"] receiving request for "+message);
            try {
                switch (queueName){
                    case "increaseBalance":
                        BalanceRequest increaseRequest = objectMapper.readValue(message, BalanceRequest.class);
                        userService.increaseBalance(increaseRequest.getId(), increaseRequest.getValue());
                        break;
                    default:
                        break;
                }
            } catch (Exception ignored) {
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
    }
}
