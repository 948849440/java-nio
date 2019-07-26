package com.summer.study.netty.hello.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class HelloClent {

    public static void main(String[] args) {

        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup);
            bootstrap.channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new HelloClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",6666).sync();
            channelFuture.channel().writeAndFlush("fkkkk!!!!!!!!!!");
            //channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            workGroup.shutdownGracefully();
        }

    }
}
