package cn.rookiex.robot;

import cn.rookiex.manager.RobotManager;
import io.netty.channel.ChannelInitializer;

/**
 * @author rookieX 2022/12/8
 */
public interface RobotFactory {
    Robot newRobot(RobotManager manager);

    RobotContext newRobotContext(RobotManager manager);

    ChannelInitializer getChannelInitializer(RobotManager manager);
}
