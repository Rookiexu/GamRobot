package cn.rookiex.coon;

import cn.rookiex.coon.message.StrMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author rookieX 2022/12/14
 */
public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int totalLength = in.readableBytes();
        int msgId = in.readInt();
        byte[] body = new byte[totalLength - 4]; //获取body的内容
        in.readBytes(body);

        StrMessage message = new StrMessage();
        message.setMsgId(msgId);
        message.setDataBytes(body);
        out.add(message);
    }
}
