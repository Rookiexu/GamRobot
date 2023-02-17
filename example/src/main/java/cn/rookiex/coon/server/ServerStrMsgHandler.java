package cn.rookiex.coon.server;

import cn.hutool.core.util.RandomUtil;
import cn.rookiex.coon.AttributeConstants;
import cn.rookiex.coon.message.StrMessage;
import cn.rookiex.coon.safe.Decrypt;
import cn.rookiex.coon.safe.key.SecretKey;
import cn.rookiex.coon.server.timer.TimerHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

/**
 * @author rookieX 2023/2/9
 */
@Log4j2
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
        if (ctx.channel().attr(AttributeConstants.CLIENT_KEY).get() == null) {
            StrMessage strMessage = new StrMessage();
            strMessage.setMsgId(-1);
            strMessage.setData(key.getKey(SecretKey.PUBLIC));
            ctx.channel().writeAndFlush(strMessage);
            log.info("连接创建 下发publicKey : " + strMessage.parseData(String.class));
        }
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StrMessage msg) throws Exception {
        int msgId = msg.getMsgId();
        if (msgId < 0){
            initKeys(ctx, msg);
            return;
        }

        TimerHolder.millisTimer.newTimeout((timeout) -> {
            ctx.channel().writeAndFlush(msg);
        }, RandomUtil.randomInt(5), TimeUnit.MILLISECONDS);
    }

    private void initKeys(ChannelHandlerContext ctx, StrMessage msg) {
        byte[] decrypt = serverDecrypt.decrypt(msg.getDataBytes());
        String clientKey = new String(decrypt);
        ctx.channel().attr(AttributeConstants.CLIENT_KEY).set(clientKey);

        StrMessage strMessage = new StrMessage();
        strMessage.setMsgId(-2);
        strMessage.setData("");
        ctx.channel().writeAndFlush(strMessage);
        log.info("服务器收到客户端加密秘钥 : " + clientKey);
        ctx.channel().attr(AttributeConstants.READY).set(true);
        return;
    }
}
