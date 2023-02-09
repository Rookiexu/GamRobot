package cn.rookiex.coon.client;

import cn.rookiex.RobotServer;
import cn.rookiex.coon.message.SimpleMessage;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.Robot;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

/**
 * @author rookieX 2022/12/14
 */
@Log4j2
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       //todo 连接活跃处理
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SimpleMessage message = (SimpleMessage) msg;
        try {
            onMessage(ctx.channel(), message);
        }catch (Exception e) {
            log.error("处理消息异常,cmd:"+message.getMsgId()+",channel:"+ctx.channel(),e);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // todo 连接断开处理
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    protected void onMessage(Channel channel, SimpleMessage message) throws Exception {
        RobotManager robotManager = RobotServer.getInstance().getRobotManager();
        String aLong = channel.attr(Robot.CHANNEL_ATTR_ID).get();
        Robot robot = robotManager.getRobotMap().get(aLong);
        robot.getRespQueue().add(message);
    }

}
