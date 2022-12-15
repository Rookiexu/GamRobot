package cn.rookiex.server;

import cn.rookiex.coon.ClientHandler;
import cn.rookiex.coon.MsgDecoder;
import cn.rookiex.coon.MsgEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author rookieX 2022/12/14
 */
public class ServerChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
//        p.addLast(new LoggingHandler(LogLevel.INFO));
        p.addLast(new LengthFieldBasedFrameDecoder(1024 * 512, 0, 4, 0, 4));
        p.addLast(new MsgDecoder());
        p.addLast(new ServerHandler());
        p.addLast(new MsgEncoder());
    }
}
