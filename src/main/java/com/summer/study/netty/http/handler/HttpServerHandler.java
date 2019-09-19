package com.summer.study.netty.http.handler;

import com.alibaba.fastjson.JSON;
import com.summer.study.netty.base.manager.HttpManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Component
@Qualifier("demoOneHttpServerHandler")
@Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler {

    @Autowired
    private HttpManager httpManager;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel();
        System.out.println("summer: HttpServerHandler channelActive");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("summer: HttpServerHandler channelRead");
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;

            if (HttpUtil.is100ContinueExpected(fullHttpRequest)) {
                send100Continue(ctx);
            }
            fullHttpRequest.content();
            System.out.println("!!!!!!" + fullHttpRequest.uri());

            httpManager.handleRequest(fullHttpRequest.uri());
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(fullHttpRequest.uri());
            System.out.println("param:" + JSON.toJSON(queryStringDecoder.parameters()));
            HttpContent httpContent = (HttpContent) msg;
            HttpHeaders httpHeaders = fullHttpRequest.headers();
            ByteBuf contentBuf = httpContent.content();
            System.out.println("contentBuf:" + contentBuf.toString(CharsetUtil.UTF_8));

            boolean isKeepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
            boolean decodeSuccess = true;
            Object respObj = "haha";
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1, decodeSuccess ? OK : BAD_REQUEST,
                    Unpooled.copiedBuffer(JSON.toJSONString(respObj), CharsetUtil.UTF_8)
            );
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            if (isKeepAlive) {
                //KeepAlive需要设置CONTENT_LENGTH
                response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.writeAndFlush(response);

            if (!isKeepAlive) {
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
            return;
        }
        //HttpObjectAggregator 用了这个会 用FullHttpRequest这个接
        if (msg instanceof HttpRequest) {
            System.out.println("HttpRequest");
            HttpRequest httpRequest = (HttpRequest) msg;
            System.out.println("uri:" + httpRequest.uri());
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.uri());
            System.out.println("param:" + JSON.toJSON(queryStringDecoder.parameters()));
        }
        if (msg instanceof HttpContent) {
            System.out.println("HttpContent");
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf contentBuf = httpContent.content();
            System.out.println("contentBuf:" + contentBuf.toString(CharsetUtil.UTF_8));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("summer: HttpServerHandler channelRead");
        cause.printStackTrace();
    }

    private void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }
}
