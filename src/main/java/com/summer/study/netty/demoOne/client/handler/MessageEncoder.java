package com.summer.study.netty.demoOne.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println("demo one MessageEncoder!!");
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeInt(1);
        byteBuf.writeInt(2);
        out.writeBytes(byteBuf);
    }
}
