package cn.rookiex.coon.server;

import cn.hutool.core.util.RandomUtil;
import cn.rookiex.coon.message.StrMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.HashedWheelTimer;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

/**
 * @author rookieX 2022/12/14
 */
@Log4j2
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.MILLISECONDS, 10);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       //todo 连接活跃处理
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        timer.newTimeout((timeout) -> {
            StrMessage message = (StrMessage) msg;
            ctx.channel().writeAndFlush(message);
        }, RandomUtil.randomInt(5), TimeUnit.MILLISECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // todo 连接断开处理
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
