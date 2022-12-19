package cn.rookiex.robot;

import cn.rookiex.coon.client.NioChannelInitializer;
import cn.rookiex.manager.RobotManager;
import io.netty.channel.ChannelInitializer;

/**
 * @author rookieX 2022/12/8
 */
public class DefaultRobotFactory implements RobotFactory {

    NioChannelInitializer initializer = new NioChannelInitializer();

    @Override
    public Robot newRobot(RobotManager manager) {
        return new Robot();
    }

    @Override
    public RobotContext newRobotContext(RobotManager manager) {

        return new DefaultRobotContext();
    }

    @Override
    public ChannelInitializer getChannelInitializer(RobotManager manager) {
        return initializer;
    }
}
