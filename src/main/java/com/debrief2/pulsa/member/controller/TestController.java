package com.debrief2.pulsa.member.controller;

import com.debrief2.pulsa.member.payload.request.TesterRequest;
import com.debrief2.pulsa.member.utils.rpc.RPCClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private String url = "amqp://lbfcxugj:eW3yKsOA0FIKKBSzuQz3dVyx5izT0C-8@toad.rmq.cloudamqp.com/lbfcxugj";
    RPCClient rpcClient;

    @GetMapping("/member")
    public String apiTester(@RequestBody TesterRequest testerRequest) throws Exception {
        rpcClient = new RPCClient(url, testerRequest.getQueue());
        return rpcClient.call(testerRequest.getMessage());
    }

}
