package com.debrief2.pulsa.member;

import com.debrief2.pulsa.member.payload.request.BalanceRequest;
import com.debrief2.pulsa.member.payload.request.UserRequest;
import com.debrief2.pulsa.member.payload.request.VerifyPinRequest;
import com.debrief2.pulsa.member.utils.rpc.RPCClient;
import com.debrief2.pulsa.member.utils.rpc.RPCServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemberApplication implements CommandLineRunner {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  RPCServer rpcServer;

  @Value("${cloudAMQP.url}")
  private String url;

  public static void main(String[] args) {
    SpringApplication.run(MemberApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
//    rpcServer.run("register");
//    rpcServer.run("login");
//    rpcServer.run("verifyPin");
//    rpcServer.run("getProfile");
//    rpcServer.run("getBalance");
//    rpcServer.run("decreaseBalance");
//    rpcServer.run("increaseBalance");

    Thread.sleep(1000);

    UserRequest userRequest = new UserRequest();
    userRequest.setName("Nabilla");
    userRequest.setEmail("nabilla@gmail.com");
    userRequest.setUsername("082272068810");
    userRequest.setPin(121245);
    RPCClient rpcClient = new RPCClient(url, "register");
    System.out.println(rpcClient.call(objectMapper.writeValueAsString(userRequest)));

    RPCClient rpcClient1 = new RPCClient(url, "login");
    System.out.println(rpcClient1.call("082272721111"));

    VerifyPinRequest request = new VerifyPinRequest();
    request.setId(1);
    request.setPin(454589);
    RPCClient rpcClient2 = new RPCClient(url, "verifyPin");
    System.out.println(rpcClient2.call(objectMapper.writeValueAsString(request)));

    RPCClient rpcClient3 = new RPCClient(url, "getProfile");
    System.out.println(rpcClient3.call("1"));

    RPCClient rpcClient4 = new RPCClient(url, "getBalance");
    System.out.println(rpcClient4.call("1"));

//    BalanceRequest balanceRequest = new BalanceRequest();
//    balanceRequest.setId(8);
//    balanceRequest.setValue(254000);
//    RPCClient rpcClient5 = new RPCClient(url, "decreaseBalance");
//    System.out.println(rpcClient5.call(objectMapper.writeValueAsString(balanceRequest)));

//    RPCClient rpcClient6 = new RPCClient(url, "increaseBalance");
//    System.out.println(rpcClient6.call(objectMapper.writeValueAsString(balanceRequest)));
  }

}
