package com.debrief2.pulsa.member;

import com.debrief2.pulsa.member.utils.rpc.RPCServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemberApplication implements CommandLineRunner {

  @Autowired
  RPCServer rpcServer;

  public static void main(String[] args) {
    SpringApplication.run(MemberApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    rpcServer.run("register");
    rpcServer.run("login");
    rpcServer.run("login");
    rpcServer.run("verifyPin");
    rpcServer.run("getProfile");
    rpcServer.run("getProfile");
    rpcServer.run("getBalance");
    rpcServer.run("getBalance");
    rpcServer.run("decreaseBalance");
    rpcServer.runPersistent("increaseBalance");
    rpcServer.run("sendOTP");
    rpcServer.run("getOTP");
    rpcServer.run("verifyOTP");
    rpcServer.run("changePin");

  }


}
