package com.summer.study.netty.hello.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

public class HelloClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("HelloClientHandler channelRead:"+msg);
        super.channelRead(ctx, msg);
    }
}
