package cn.rookiex.coon.client;

import cn.hutool.Hutool;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.rookiex.RobotServer;
import cn.rookiex.coon.message.JsonMessage;
import cn.rookiex.coon.message.StrMessage;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.Robot;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.HashedWheelTimer;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.util.concurrent.TimeUnit;

/**
 * @author rookieX 2023/2/9
 */
@Log4j2
public class ClientStrMsgHandler extends SimpleChannelInboundHandler<StrMessage> {

    private final HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.MILLISECONDS, 5);

    private AES aes;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StrMessage msg) throws Exception {
        if (msg.getMsgId() == -1){
            String publicKey = msg.parseData(String.class);
            String aesKey = RandomUtil.randomInt(1024) + "";
            this.aes = SecureUtil.aes(aesKey.getBytes());

            String format = secretKey.getFormat();
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
