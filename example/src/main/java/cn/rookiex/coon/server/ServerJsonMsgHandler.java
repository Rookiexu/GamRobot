package cn.rookiex.coon.server;

import cn.hutool.core.util.RandomUtil;
import cn.rookiex.coon.message.JsonMessage;
import cn.rookiex.coon.server.timer.TimerHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author rookieX 2023/2/9
 */
public class ServerJsonMsgHandler extends SimpleChannelInboundHandler<JsonMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JsonMessage msg) throws Exception {
        TimerHolder.millisTimer.newTimeout((timeout) -> {
            ctx.channel().writeAndFlush(msg);
        }, RandomUtil.randomInt(5), TimeUnit.MILLISECONDS);
    }
}
