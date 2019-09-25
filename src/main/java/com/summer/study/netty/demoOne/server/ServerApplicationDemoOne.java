package com.summer.study.netty.demoOne.server;

import com.summer.study.netty.demoOne.server.handler.GameDemoMessageDecoder;
import com.summer.study.netty.demoOne.server.handler.MessageDecoder;
import com.summer.study.netty.demoOne.server.handler.SerializeMessageDecoder;
import com.summer.study.netty.demoOne.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class ServerApplicationDemoOne {
    @Value("${boss.thread.count-webSocketServer}")
    private int bossCount;

    @Value("${worker.thread.count-webSocketServer}")
    private int workerCount;

    @Value("${tcp.port-demoOneServer}")
    private int tcpPort;

    @Value("${switch.demoOne-server}")
    private boolean startSwitch;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;

    private ChannelFuture channelFuture;

    @PostConstruct
    private void startServer() {
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
                            //pipeline.addLast("http-codec", new HttpServerCodec());
                            //pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
                            //pipeline.addLast(new MessageDecoder());
                            pipeline.addLast(new GameDemoMessageDecoder());
                            //pipeline.addLast(new SerializeMessageDecoder());
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            channelFuture = bootstrap.bind(tcpPort).sync();
            System.out.println("demo one server start !!");
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
