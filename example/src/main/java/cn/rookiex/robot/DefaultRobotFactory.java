package cn.rookiex.robot;

import cn.rookiex.coon.client.initializer.ClientStrChannelInitializer;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.ctx.RobotContext;
import cn.rookiex.robot.ctx.RobotCtx;
import io.netty.channel.ChannelInitializer;

/**
 * @author rookieX 2022/12/8
 */
public class DefaultRobotFactory implements RobotFactory {

    ClientStrChannelInitializer initializer = new ClientStrChannelInitializer();

    @Override
    public Robot newRobot(RobotManager manager) {
        return new Robot();
    }

    @Override
    public RobotContext newRobotContext(RobotManager manager) {
        return new RobotCtx();
    }

    @Override
    public ChannelInitializer getChannelInitializer(RobotManager manager) {
        return initializer;
    }
}
