package com.summer.study.netty.heartbeatDesign.client;

import com.summer.study.netty.base.manager.HeartbeatManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class HeartBeatClient {

    @Autowired
    private HeartbeatManager heartbeatManager;

    @Value("${tcp.port-heartbeatServer}")
    private int PORT;

    @Value("${switch.heartBeat-client}")
    private boolean startSwitch;

    private EventLoopGroup workGroup;

    private Bootstrap bootstrap;

    @PostConstruct
    private void startClient() {
        if(!startSwitch){
            return;
        }
        workGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline ch = channel.pipeline();
                        ch.addLast("string decoder", new StringDecoder(CharsetUtil.UTF_8));
                        ch.addLast("string encoder", new StringEncoder(CharsetUtil.UTF_8));
                        ch.addLast("idleStateHandler", new IdleStateHandler(10, 0, 0));
                        ch.addLast("clientHandler", new ClientHandler());
                    }
                });
        System.out.println("HeartBeatClient start !!");
    }

    public void connect(){
        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", PORT).sync();
            heartbeatManager.initClientChannel(channelFuture.channel());
            channelFuture.channel().writeAndFlush("fkkkk !!");
            System.out.println("HeartBeatClient connect !!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
