package com.summer.study.netty.http.demo1;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead0!!!!!!!!!!!!");
        if(msg instanceof FullHttpRequest){
            FullHttpRequest fullHttpRequest = (FullHttpRequest)msg;
            fullHttpRequest.content();
            System.out.println("!!!!!!"+fullHttpRequest.uri());
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(fullHttpRequest.uri());
            System.out.println("param:" + JSON.toJSON(queryStringDecoder.parameters()));
            HttpContent httpContent = (HttpContent)msg;
            HttpHeaders httpHeaders = fullHttpRequest.headers();
            //System.out.println("header lenght:"+httpHeaders.);
            ByteBuf contentBuf = httpContent.content();
            System.out.println("contentBuf:"+ contentBuf.toString(CharsetUtil.UTF_8));
            return;
        }
        if(msg instanceof HttpRequest){
            System.out.println("HttpRequest");
            HttpRequest httpRequest = (HttpRequest) msg;
            System.out.println("uri:"+httpRequest.uri());
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.uri());
            System.out.println("param:" + JSON.toJSON(queryStringDecoder.parameters()));
        }
        if(msg instanceof HttpContent){
            System.out.println("HttpContent");
            HttpContent httpContent = (HttpContent)msg;
            ByteBuf contentBuf = httpContent.content();
            System.out.println("contentBuf:"+ contentBuf.toString(CharsetUtil.UTF_8));
        }

    }
}
