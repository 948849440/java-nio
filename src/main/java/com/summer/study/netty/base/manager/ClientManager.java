package com.summer.study.netty.base.manager;

import com.summer.study.netty.demoOne.client.ClientApplicationDemoOne;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientManager {

    @Autowired
    private ClientApplicationDemoOne clientApplicationDemoOne;

    private Channel channal;

    public void initChannal(Channel channal){
        this.channal = channal;
    }

    public void sendMessage(Object message){
        channal.writeAndFlush(message);
    }

    public void startClient(){
        if(channal != null && channal.isActive()){
            return;
        }
        clientApplicationDemoOne.startServer();
    }
}
