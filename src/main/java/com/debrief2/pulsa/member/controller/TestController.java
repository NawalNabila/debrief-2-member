package com.debrief2.pulsa.member.controller;

import com.debrief2.pulsa.member.payload.request.TesterRequest;
import com.debrief2.pulsa.member.utils.rpc.RPCClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private String url = "amqp://ynjauqav:K83KvUARdw7DyYLJF2_gt2RVzO-NS2YM@lively-peacock.rmq.cloudamqp.com/ynjauqav";
    RPCClient rpcClient;

    @GetMapping("/member")
    public String apiTester(@RequestBody TesterRequest testerRequest) throws Exception {
        rpcClient = new RPCClient(url, testerRequest.getQueue());
        return rpcClient.call(testerRequest.getMessage());
    }

}
