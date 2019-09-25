package com.summer.study.netty.base.manager;

import com.summer.study.netty.heartbeatDesign.client.HeartBeatClient;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class HeartbeatManager {

    @Autowired
    private HeartBeatClient heartBeatClient;

    private static HeartbeatManager INTANCE;

    private Channel clientChannel;

    private AtomicInteger heartBeatErrorTimes;

    private int MAX_ERROR_TIMES;

    @PostConstruct
    private void init(){
        INTANCE = this;
    }

    public void initClientChannel(Channel ch) {
        this.clientChannel = ch;
    }

    public boolean isActive() {
        return clientChannel != null && clientChannel.isActive();
    }

    public Channel getClientChannel() {
        return clientChannel;
    }

    public void writeMessage(Object message){
        if(getClientChannel().isActive()){
            getClientChannel().writeAndFlush(message);
            clearHeartBeatErrorTimes();
        }else {
            System.out.println("channel not active");
        }
    }

    public static HeartbeatManager getINTANCE() {
        return INTANCE;
    }

    public void addHeartBeatErrorTimes(int alterValue){
        heartBeatErrorTimes.addAndGet(alterValue);
    }

    public void clearHeartBeatErrorTimes(){
        heartBeatErrorTimes.set(0);
    }

    public int getHeartBeatErrorTimes() {
        return heartBeatErrorTimes.get();
    }

    public void verifyHeartBeatErrorTimes() {
        if(getHeartBeatErrorTimes() > MAX_ERROR_TIMES){
            System.out.println("error times max !!");
            reconnect();
        }
    }

    public void reconnect() {
        System.out.println("reconnect");
        heartBeatClient.connect();
    }
}
