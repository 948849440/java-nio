package com.summer.study.netty.base.controller;

import com.summer.study.netty.base.annotation.HttpController;
import com.summer.study.netty.base.annotation.MethodMapper;
import com.summer.study.netty.base.manager.HeartbeatManager;
import org.springframework.beans.factory.annotation.Autowired;

@HttpController(value = "/hb")
public class HeartBeatController {

    @Autowired
    private HeartbeatManager heartbeatManager;

    @MethodMapper(value = "/init")
    public void init() {
        heartbeatManager.reconnect();
    }

    @MethodMapper(value = "/test")
    public void test() {
        heartbeatManager.writeMessage("happy !!");
    }

    @MethodMapper(value = "/serverStop")
    public void serverStop() {
        heartbeatManager.writeMessage("serverStop");
    }

    @MethodMapper(value = "/isActive")
    public void isActive() {
        System.out.println("isActive:" + heartbeatManager.isActive());
    }

    @MethodMapper(value = "/clientStop")
    public void clientStop() {
        heartbeatManager.clientStop("clientStop");
    }

}
