package com.summer.study.netty.http.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.DefaultThreadFactory;

public class HttpServer {

    private int port = 10086;

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("HttpBossGroup"));
    private EventLoopGroup workGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("HttpBossGroup"));

    public void start() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    //.childOption(ChannelOption.TCP_NODELAY, true)
                    //.childOption(ChannelOption.SO_KEEPALIVE, true)
                    //.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-codec", new HttpServerCodec());
                            pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(65536));
                            pipeline.addLast("http-handler", new HttpServerHandler());
                        }
                    });
            bootstrap.bind(port).sync();
            System.out.println("http server start!!");
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            e.printStackTrace();
        }


    }
}
