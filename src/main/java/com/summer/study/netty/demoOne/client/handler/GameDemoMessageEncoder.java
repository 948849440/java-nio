package com.summer.study.netty.demoOne.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class GameDemoMessageEncoder extends MessageToByteEncoder {

    private byte[] magicArr = {1, 0, 2, 4};

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println("demo one MessageEncoder!!");
        out.writeBytes(magicArr);
        System.out.println(msg);
        out.writeInt((int) msg);
    }
}
