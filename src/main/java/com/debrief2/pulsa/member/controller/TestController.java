package com.debrief2.pulsa.member.controller;

import com.debrief2.pulsa.member.exception.ServiceException;
import com.debrief2.pulsa.member.payload.request.TesterRequest;
import com.debrief2.pulsa.member.utils.rpc.RPCClient;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private String url = "amqp://ynjauqav:K83KvUARdw7DyYLJF2_gt2RVzO-NS2YM@lively-peacock.rmq.cloudamqp.com/ynjauqav";
    RPCClient rpcClient;
    String response = "";

    @GetMapping("/member")
    public String apiTester(@RequestBody TesterRequest testerRequest) throws Exception {
        try {
            rpcClient = new RPCClient(url, testerRequest.getQueue());
            response = rpcClient.call(testerRequest.getMessage());
            return response;
        } catch (ServiceException | NullPointerException exception) {
            return response = exception.getMessage();
        } catch (InvalidFormatException | NumberFormatException e) {
            return response = "invalid request format";
        }
    }

}
