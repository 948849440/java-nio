package com.summer.study.netty.base.controller;

import com.summer.study.netty.base.annotation.HttpController;
import com.summer.study.netty.base.annotation.MethodMapper;
import com.summer.study.netty.base.manager.ClientManager;
import com.summer.study.netty.demoOne.message.ReqSayHallo;
import org.springframework.beans.factory.annotation.Autowired;

@HttpController(value = "/Base")
public class BaseController {

    @Autowired
    private ClientManager clientManager;

    @MethodMapper(value = "/demo")
    public void demo(){
        System.out.println("demo!!!");
    }

    @MethodMapper(value = "/test")
    public void test(){
        clientManager.startClient();
        clientManager.sendMessage("123");
    }

    @MethodMapper(value = "/smt")
    public void serializeMessageTest(){
        clientManager.startClient();
        ReqSayHallo sayHallo = new ReqSayHallo();
        sayHallo.setId(66);
        sayHallo.setText("fuck");
        clientManager.sendMessage(sayHallo);
    }

    @MethodMapper(value = "/tg")
    public void testGame(){
        clientManager.startClient();
        clientManager.sendMessage(1024);
    }

}
