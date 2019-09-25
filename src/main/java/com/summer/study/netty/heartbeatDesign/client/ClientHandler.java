package com.summer.study.netty.heartbeatDesign.client;

import com.summer.study.netty.base.manager.HeartbeatManager;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("msg:" + msg);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            System.out.println("client IdleStateEvent:" + evt);
            sendHeartBeat(ctx.channel());
        }
        super.userEventTriggered(ctx, evt);
    }

    private void sendHeartBeat(Channel channel) {
        channel.writeAndFlush("client send heartBeat").addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                System.out.println("client send HeartBeat callback");
                if(channelFuture.isSuccess()){
                    HeartbeatManager.getINTANCE().clearHeartBeatErrorTimes();
                    System.out.println("client send HeartBeat success");
                }else {
                    HeartbeatManager.getINTANCE().addHeartBeatErrorTimes(1);
                    HeartbeatManager.getINTANCE().verifyHeartBeatErrorTimes();
                    System.out.println("client send HeartBeat error");
                }
            }
        });
    }
}
