package cn.rookiex.coon.client;

import cn.hutool.core.util.RandomUtil;
import cn.rookiex.RobotServer;
import cn.rookiex.coon.message.JsonMessage;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.Robot;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;


/**
 * @author rookieX 2023/2/9
 */
@Log4j2
public class ClientJsonMsgHandler extends SimpleChannelInboundHandler<JsonMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JsonMessage msg) throws Exception {
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
