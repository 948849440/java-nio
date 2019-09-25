package com.summer.study.netty.heartbeatDesign.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class HeartBeatServer {

    @Value("${tcp.port-heartbeatServer}")
    private int PORT;

    @Value("${switch.heartBeat-server}")
    private boolean startSwitch;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;

    @PostConstruct
    private void startServer() {
        if(!startSwitch){
            return;
        }
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bossGroup = new NioEventLoopGroup(1);
            workGroup = new NioEventLoopGroup(2);
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("string decoder", new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast("string encoder", new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0 , 32));
                            pipeline.addLast("serverHandler", new ServerHandler());
                        }
                    });
            bootstrap.bind(PORT).sync();
            System.out.println("HeartBeatServer start !!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


}
