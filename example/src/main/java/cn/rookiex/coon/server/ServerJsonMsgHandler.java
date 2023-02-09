package cn.rookiex.coon.server;

import cn.hutool.core.util.RandomUtil;
import cn.rookiex.coon.message.JsonMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.HashedWheelTimer;

import java.util.concurrent.TimeUnit;

/**
 * @author rookieX 2023/2/9
 */
public class ServerJsonMsgHandler extends SimpleChannelInboundHandler<JsonMessage> {
    private final HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.MILLISECONDS, 5);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JsonMessage msg) throws Exception {
        timer.newTimeout((timeout) -> {
            ctx.channel().writeAndFlush(msg);
        }, RandomUtil.randomInt(5), TimeUnit.MILLISECONDS);
    }
}
