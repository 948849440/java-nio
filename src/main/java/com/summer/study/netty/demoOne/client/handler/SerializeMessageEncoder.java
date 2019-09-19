package com.summer.study.netty.demoOne.client.handler;

import com.summer.study.netty.base.annotation.MessageId;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class SerializeMessageEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //发送消息id
        MessageId anno = msg.getClass().getAnnotation(MessageId.class);
        if(anno == null){
            return;
        }
        int msgId = anno.id();
        out.writeInt(msgId);

        //序列化
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(msg);
        objectOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        out.writeBytes(bytes);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
