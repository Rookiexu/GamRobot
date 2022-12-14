package cn.rookiex.coon;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author rookieX 2022/12/14
 */
public class NioChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new LengthFieldBasedFrameDecoder(1024 * 512, 0, 4, 0, 4));
        p.addLast(new MsgDecoder());
        p.addLast(new ClientHandler());
        p.addLast(new MsgEncoder());
    }
}
