package cn.rookiex.coon;

import cn.rookiex.coon.message.StrMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author rookieX 2022/12/14
 */
public class MsgEncoder extends MessageToByteEncoder<StrMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, StrMessage msg, ByteBuf out) throws Exception {
        int msgId = msg.getMsgId();
        byte[] bytes = msg.getDataBytes();
        int length = (bytes.length + 4);
        out.writeInt(length);
        out.writeInt(msgId);
        out.writeBytes(bytes);
    }
}
