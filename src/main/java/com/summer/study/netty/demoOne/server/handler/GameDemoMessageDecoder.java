package com.summer.study.netty.demoOne.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

public class GameDemoMessageDecoder extends ByteToMessageDecoder {

    private byte[] magicArr = {1,0,2,4};

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("demo one MessageDecoder!!");

        int magicHeaderSize = magicArr.length;
        byte[] inMagic = new byte[magicHeaderSize];
        in.readBytes(inMagic);
        if(!Arrays.equals(inMagic, magicArr)){
            System.out.println(" magic incorrect !!");
            return;
        }
        int intMsg = in.readInt();
        System.out.println("get intMsg:"+intMsg);
        out.add(intMsg);

    }
}
