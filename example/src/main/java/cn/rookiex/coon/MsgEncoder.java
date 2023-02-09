package cn.rookiex.coon;

import cn.rookiex.coon.message.SimpleMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author rookieX 2022/12/14
 */
public class MsgEncoder extends MessageToByteEncoder<SimpleMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, SimpleMessage msg, ByteBuf out) throws Exception {
        int msgId = msg.getMsgId();
        byte[] bytes = msg.getDataBytes();
        int length = (bytes.length + 4);
        out.writeInt(length);
        out.writeInt(msgId);
        out.writeBytes(bytes);
    }
}
