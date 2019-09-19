package com.summer.study.netty.webSocket.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;

public class WebSocketFrameToByteBufDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof TextWebSocketFrame){
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame)msg;
            textWebSocketFrame.retain();

            //放到管道尾部
            ctx.writeAndFlush(textWebSocketFrame);
            ReferenceCountUtil.release(msg);
        }else if(msg instanceof BinaryWebSocketFrame){
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame)msg;
            //发给下一个handler
            ctx.fireChannelRead(binaryWebSocketFrame);
        }
    }
}
