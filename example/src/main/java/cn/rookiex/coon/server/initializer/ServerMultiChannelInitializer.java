package cn.rookiex.coon.server.initializer;

import cn.rookiex.coon.ErrMessageHandler;
import cn.rookiex.coon.MultipleMsgDecoder;
import cn.rookiex.coon.MultipleMsgEncoder;
import cn.rookiex.coon.server.ServerJsonMsgHandler;
import cn.rookiex.coon.server.ServerStrMsgHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author rookieX 2022/12/14
 */
public class ServerMultiChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new LengthFieldBasedFrameDecoder(1024 * 512, 0, 4, 0, 4));
        p.addLast(new MultipleMsgDecoder());
        p.addLast(new ServerStrMsgHandler());
        p.addLast(new ServerJsonMsgHandler());
        p.addLast(new ErrMessageHandler());
        p.addLast(new MultipleMsgEncoder());
    }
}
