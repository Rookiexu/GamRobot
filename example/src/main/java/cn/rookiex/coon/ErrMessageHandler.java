package cn.rookiex.coon;

import cn.rookiex.coon.message.ErrMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

/**
 * @author rookieX 2023/2/9
 */
@Log4j2
public class ErrMessageHandler extends SimpleChannelInboundHandler<ErrMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ErrMessage msg) throws Exception {
        log.error("协议解析错误 : " + msg.getMsgId());
    }
}
