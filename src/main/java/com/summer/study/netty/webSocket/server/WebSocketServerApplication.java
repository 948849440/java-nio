package com.summer.study.netty.webSocket.server;

import com.summer.study.netty.webSocket.server.handler.BaseFrameDecoder;
import com.summer.study.netty.webSocket.server.handler.WebSocketFrameToByteBufDecoder;
import com.summer.study.netty.webSocket.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class WebSocketServerApplication {
    @Value("${boss.thread.count-webSocketServer}")
    private int bossCount;

    @Value("${worker.thread.count-webSocketServer}")
    private int workerCount;

    @Value("${tcp.port-webSocketServer}")
    private int tcpPort;

    @Value("${switch.webSocket-server}")
    private boolean startSwitch;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;

    private ChannelFuture channelFuture;

    @PostConstruct
    private void startServer(){
        if(!startSwitch){
            return;
        }
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bossGroup = new NioEventLoopGroup(bossCount);
            workGroup = new NioEventLoopGroup(workerCount);
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("IdleStateHandle", new IdleStateHandler(300, 0, 0));
                            pipeline.addLast("http-codec", new HttpServerCodec());
                            pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
                            pipeline.addLast("websocket", new WebSocketServerProtocolHandler("/"));
                            pipeline.addLast(new WebSocketFrameToByteBufDecoder());
                            pipeline.addLast(new BaseFrameDecoder());
                            pipeline.addLast(new WebSocketHandler());
                        }
                    });
            channelFuture = bootstrap.bind(tcpPort).sync();
            System.out.println("webSocket server start !!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @PreDestroy
    private void stop() throws InterruptedException {
        channelFuture.channel().closeFuture().sync();
    }
}
