package com.summer.study.netty.demoOne.server.handler;

import com.summer.study.netty.base.manager.SerializeManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class SerializeMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int messageId = in.readInt();
        Class messageClassType = SerializeManager.getInstance().getMessageClassTypeById(messageId);

        int size = in.readableBytes();
        byte[] bytes = new byte[size];
        in.readBytes(bytes);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object obj = objectInputStream.readObject();
        out.add(changeObjectToClass(obj, messageClassType));
    }

    private <T> T changeObjectToClass(Object inObj, Class<T> messageClassType) {
        return (T) inObj;
    }
}
