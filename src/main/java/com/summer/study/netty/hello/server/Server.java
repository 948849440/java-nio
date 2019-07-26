package com.summer.study.netty.hello.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class Server {

    public static void main(String[] args) {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new HelloHandler());

                        }
                    });
            System.out.println("ggg!!");
            ChannelFuture channelFuture = b.bind(6666).sync();
            channelFuture.channel().closeFuture().sync();
            System.out.println("server start!!");
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            e.printStackTrace();
        }
    }
}
