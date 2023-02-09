package cn.rookiex.coon;

import cn.rookiex.coon.message.SimpleMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author rookieX 2023/2/9
 */
public class MultipleMsgDecoder  extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int totalLength = in.readableBytes();
        short msgType = in.readShort();
        int msgId = in.readInt();
        byte[] body = new byte[totalLength - 6]; //获取body的内容
        in.readBytes(body);

        switch (msgType){
            case 1:
        }
        SimpleMessage message = new SimpleMessage();
        message.setMsgId(msgId);
        message.setDataBytes(body);
        out.add(message);
    }
}
