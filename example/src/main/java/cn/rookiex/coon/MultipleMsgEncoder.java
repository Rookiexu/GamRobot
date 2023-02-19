package cn.rookiex.coon;

import cn.hutool.core.codec.Base64;
import cn.rookiex.coon.safe.AesDecrypt;
import cn.rookiex.coon.safe.AesEncrypt;
import cn.rookiex.coon.safe.Decrypt;
import cn.rookiex.coon.safe.Encrypt;
import cn.rookiex.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author rookieX 2023/2/9
 */
public class MultipleMsgEncoder extends MessageToByteEncoder<Message> {

    private Encrypt encrypt;

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        int msgId = msg.getMsgId();
        byte[] bytes = msg.getDataBytes();

        if (msgId >= 0){
            bytes = encryptMsg(ctx, bytes);
        }

        int length = (bytes.length + 2 + 4);
        out.writeInt(length);
        out.writeShort(msg.msgType());
        out.writeInt(msgId);
        out.writeBytes(bytes);
    }

    private byte[] encryptMsg(ChannelHandlerContext ctx, byte[] bytes) {
        //加密准备完成前不加密消息
        if (ctx.channel().attr(AttributeConstants.READY).get()) {
            String s = ctx.channel().attr(AttributeConstants.CLIENT_KEY).get();
            if (this.encrypt == null) {
                this.encrypt = new AesEncrypt();
                byte[] decode = Base64.decode(s);
                this.encrypt.setSecretKey(decode);
            }
            bytes = this.encrypt.encrypt(bytes);
        }
        return bytes;
    }
}
