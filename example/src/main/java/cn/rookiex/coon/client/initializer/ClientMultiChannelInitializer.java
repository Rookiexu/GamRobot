package cn.rookiex.coon.client.initializer;

import cn.rookiex.coon.ErrMessageHandler;
import cn.rookiex.coon.MultipleMsgDecoder;
import cn.rookiex.coon.MultipleMsgEncoder;
import cn.rookiex.coon.client.ClientJsonMsgHandler;
import cn.rookiex.coon.client.ClientStrMsgHandler;
import cn.rookiex.coon.safe.RsaEncrypt;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author rookieX 2022/12/14
 */
public class ClientMultiChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new LoggingHandler());
        p.addLast(new LengthFieldBasedFrameDecoder(1024 * 512, 0, 4, 0, 4));
        p.addLast(new MultipleMsgDecoder());
        p.addLast(new ClientStrMsgHandler(RsaEncrypt.getDefault()));
        p.addLast(new ClientJsonMsgHandler());
        p.addLast(new ErrMessageHandler());
        p.addLast(new MultipleMsgEncoder());
    }
}
