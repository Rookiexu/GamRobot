package cn.rookiex.coon.server;

import cn.hutool.core.util.RandomUtil;
import cn.rookiex.coon.AttributeConstants;
import cn.rookiex.coon.message.StrMessage;
import cn.rookiex.coon.safe.Decrypt;
import cn.rookiex.coon.safe.key.SecretKey;
import cn.rookiex.coon.server.timer.TimerHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.HashedWheelTimer;

import java.util.concurrent.TimeUnit;

/**
 * @author rookieX 2023/2/9
 */
public class ServerStrMsgHandler extends SimpleChannelInboundHandler<StrMessage> {

    private final Decrypt serverDecrypt;

    private final SecretKey key;

    public ServerStrMsgHandler(Decrypt encrypt, SecretKey key){
        this.key = key;
        this.serverDecrypt = encrypt;
        this.serverDecrypt.setSecretKey(key);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().attr(AttributeConstants.SERVER_KEY) == null) {
            StrMessage strMessage = new StrMessage();
            strMessage.setMsgId(-1);
            strMessage.setData(key.getPublicKey());
        }
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StrMessage msg) throws Exception {
        int msgId = msg.getMsgId();
        if (msgId == -1){
            String s = msg.parseData(String.class);
            byte[] decrypt = serverDecrypt.decrypt(s.getBytes());
            ctx.channel().attr(AttributeConstants.CLIENT_KEY).set(new String(decrypt));
        }

        TimerHolder.millisTimer.newTimeout((timeout) -> {
            ctx.channel().writeAndFlush(msg);
        }, RandomUtil.randomInt(5), TimeUnit.MILLISECONDS);
    }
}
