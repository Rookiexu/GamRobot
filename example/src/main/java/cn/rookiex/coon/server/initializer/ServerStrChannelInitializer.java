package cn.rookiex.coon.server.initializer;

import cn.rookiex.coon.MsgDecoder;
import cn.rookiex.coon.MsgEncoder;
import cn.rookiex.coon.server.ServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author rookieX 2022/12/14
 */
public class ServerStrChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new LengthFieldBasedFrameDecoder(1024 * 512, 0, 4, 0, 4));
        p.addLast(new MsgDecoder());
        p.addLast(new IdleStateHandler(30, 30, 30, TimeUnit.SECONDS));
        p.addLast(new ServerHandler());
        p.addLast(new MsgEncoder());
    }
}
