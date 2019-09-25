package com.summer.study.netty.webSocket.client;

import com.summer.study.netty.base.manager.ClientManager;
import com.summer.study.netty.webSocket.client.handler.MessageDecoder;
import com.summer.study.netty.webSocket.client.handler.MessageEncoder;
import com.summer.study.netty.webSocket.server.handler.BaseFrameDecoder;
import com.summer.study.netty.webSocket.server.handler.WebSocketFrameToByteBufDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class WebSocketClientApplication {

    @Autowired
    private ClientManager clientManager;

    @Value("${boss.thread.count-webSocketServer}")
    private int bossCount;

    @Value("${worker.thread.count-webSocketServer}")
    private int workerCount;

    @Value("${tcp.port-webSocketServer}")
    private int tcpPort;

    @Value("${switch.webSocket-client}")
    private boolean startSwitch;

    private EventLoopGroup workGroup;

    private ChannelFuture channelFuture;

    @PostConstruct
    private void startServer() {
        if(!startSwitch){
            return;
        }
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
                            //pipeline.addLast(new MessageDecoder());
                            pipeline.addLast(new MessageEncoder());
                        }
                    });
            channelFuture = bootstrap.connect("127.0.0.1", tcpPort).sync();
            clientManager.initChannal(channelFuture.channel());
            System.out.println("webSocket client start !!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            workGroup.shutdownGracefully();
        }
    }

    @PreDestroy
    private void stop() throws InterruptedException {
        channelFuture.channel().closeFuture().sync();
    }
}
