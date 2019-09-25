package com.summer.study.netty.demoOne.client;

import com.summer.study.netty.base.manager.ClientManager;
import com.summer.study.netty.demoOne.client.handler.GameDemoMessageEncoder;
import com.summer.study.netty.demoOne.client.handler.MessageEncoder;
import com.summer.study.netty.demoOne.client.handler.SerializeMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientApplicationDemoOne {

    @Autowired
    private ClientManager clientManager;

    @Value("${boss.thread.count-webSocketServer}")
    private int bossCount;

    @Value("${worker.thread.count-webSocketServer}")
    private int workerCount;

    @Value("${tcp.port-demoOneServer}")
    private int tcpPort;

    @Value("${switch.demoOne-server}")
    private boolean startSwitch;

    private EventLoopGroup workGroup;

    private ChannelFuture channelFuture;

    public void startServer() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            workGroup = new NioEventLoopGroup(workerCount);
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //pipeline.addLast(new MessageEncoder());
                            pipeline.addLast(new GameDemoMessageEncoder());
                            //pipeline.addLast(new SerializeMessageEncoder());
                        }
                    });
            channelFuture = bootstrap.connect("127.0.0.1", tcpPort).sync();
            clientManager.initChannal(channelFuture.channel());
            channelFuture.channel().closeFuture().addListener( future -> {
                System.out.println("client auto disconnect");
            });
            System.out.println("client start !!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            workGroup.shutdownGracefully();
        }
    }
}
