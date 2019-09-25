package com.summer.study.netty.base.controller;

import com.summer.study.netty.base.annotation.HttpController;
import com.summer.study.netty.base.annotation.MethodMapper;
import com.summer.study.netty.base.manager.ClientManager;
import com.summer.study.netty.base.manager.HeartbeatManager;
import com.summer.study.netty.demoOne.message.ReqSayHallo;
import com.summer.study.netty.heartbeatDesign.client.HeartBeatClient;
import org.springframework.beans.factory.annotation.Autowired;

@HttpController(value = "/hb")
public class HeartBeatController {

    @Autowired
    private HeartbeatManager heartbeatManager;

    @MethodMapper(value = "/init")
    public void init(){
        System.out.println("/hb/init !!!");
        heartbeatManager.reconnect();
    }

    @MethodMapper(value = "/test")
    public void test(){
        System.out.println("/hb/test !!!");
        heartbeatManager.writeMessage("happy !!");
    }

}
