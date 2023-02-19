package cn.rookiex.coon;

import cn.hutool.core.codec.Base64;
import cn.rookiex.coon.message.ErrMessage;
import cn.rookiex.coon.message.JsonMessage;
import cn.rookiex.coon.message.MessageConstants;
import cn.rookiex.coon.message.StrMessage;
import cn.rookiex.coon.safe.AesDecrypt;
import cn.rookiex.coon.safe.Decrypt;
import cn.rookiex.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author rookieX 2023/2/9
 */
public class MultipleMsgDecoder extends ByteToMessageDecoder {

    private Decrypt decrypt;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int totalLength = in.readableBytes();
        short msgType = in.readShort();
        int msgId = in.readInt();

        //约定的加密通信消息
        byte[] body = new byte[totalLength - 6]; //获取body的内容
        in.readBytes(body);
        Message message = new StrMessage();//获取body的内容
        if (msgId >= 0){
            body = decryptMsg(ctx, body);
            switch (msgType) {
                case MessageConstants.STR:
                    message = new StrMessage();
                    break;
                case MessageConstants.JSON:
                    message = new JsonMessage();
                    break;
                default:
                    message = new ErrMessage();
                    break;
            }
        }
        message.setMsgId(msgId);
        message.setDataBytes(body);
        out.add(message);
    }

    private byte[] decryptMsg(ChannelHandlerContext ctx, byte[] body) {
        if (ctx.channel().attr(AttributeConstants.READY).get()) {
            String s = ctx.channel().attr(AttributeConstants.CLIENT_KEY).get();
            if (this.decrypt == null) {
                this.decrypt = new AesDecrypt();
                byte[] decode = Base64.decode(s);
                this.decrypt.setSecretKey(decode);
            }
            body = this.decrypt.decrypt(body);
        }
        return body;
    }
}
