package cn.rookiex.coon;

import cn.rookiex.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author rookieX 2023/2/9
 */
public class MultipleMsgEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        int msgId = msg.getMsgId();
        byte[] bytes = msg.getDataBytes();
        int length = (bytes.length + 2 + 4);
        out.writeInt(length);
        out.writeShort(msg.msgType());
        out.writeInt(msgId);
        out.writeBytes(bytes);
    }
}
