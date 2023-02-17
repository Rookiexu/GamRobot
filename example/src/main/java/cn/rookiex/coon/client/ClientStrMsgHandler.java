package cn.rookiex.coon.client;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.symmetric.AES;
import cn.rookiex.RobotServer;
import cn.rookiex.coon.AttributeConstants;
import cn.rookiex.coon.message.StrMessage;
import cn.rookiex.coon.safe.Encrypt;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.Robot;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;


/**
 * @author rookieX 2023/2/9
 */
@Log4j2
public class ClientStrMsgHandler extends SimpleChannelInboundHandler<StrMessage> {

    private AES aes;

    private final Encrypt serverEncrypt;

    public ClientStrMsgHandler(Encrypt encrypt){
        this.serverEncrypt = encrypt;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StrMessage msg) throws Exception {
        if (msg.getMsgId() < 0){
            initKeys(ctx, msg);
            return;
        }

        try {
            RobotManager robotManager = RobotServer.getInstance().getRobotManager();
            String aLong = ctx.channel().attr(Robot.CHANNEL_ATTR_ID).get();
            Robot robot = robotManager.getRobotMap().get(aLong);
            robot.getRespQueue().add(msg);
        }catch (Exception e){
            log.error(e,e);
            throw e;
        }
    }

    private void initKeys(ChannelHandlerContext ctx, StrMessage msg) throws NoSuchAlgorithmException {
        RobotManager robotManager = RobotServer.getInstance().getRobotManager();
        String aLong = ctx.channel().attr(Robot.CHANNEL_ATTR_ID).get();
        Robot robot = robotManager.getRobotMap().get(aLong);
        switch (msg.getMsgId()){
            case -1:
                addKeys(ctx, msg, robot);
                return;
            case -2:
                ctx.channel().attr(AttributeConstants.READY).set(true);
                robot.setReady(true);
                log.info(robot.getFullName() + " 收到服务器ack返回,客户端连接创建完成");
        }
    }

    private void addKeys(ChannelHandlerContext ctx, StrMessage msg, Robot robot) throws NoSuchAlgorithmException {
        //服务器公钥加载
        String publicKey = msg.parseData(String.class);
        serverEncrypt.setSecretKey(publicKey);
        ctx.channel().attr(AttributeConstants.SERVER_KEY).set(publicKey);

        //生成aes秘钥
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        byte[] encoded = kg.generateKey().getEncoded();
        String encode = Base64.encode(encoded);
        ctx.channel().attr(AttributeConstants.CLIENT_KEY).set(encode);

        log.info(robot.getFullName() + " ,收到服务器公钥 : " + publicKey);
        log.info(robot.getFullName() + " ,生成对称秘钥base64 : " + encode);
        byte[] encrypt = serverEncrypt.encrypt(encode.getBytes());
        StrMessage strMessage = new StrMessage();
        strMessage.setMsgId(-1);
        strMessage.setDataBytes(encrypt);
        ctx.channel().writeAndFlush(strMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.info(cause, cause);
    }
}
