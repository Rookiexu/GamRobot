package cn.rookiex.robot;

import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.ctx.RobotContext;
import io.netty.channel.ChannelInitializer;

/**
 * @author rookieX 2022/12/8
 */
public interface RobotFactory {
    Robot newRobot(RobotManager manager);

    RobotContext newRobotContext(RobotManager manager);

    ChannelInitializer getChannelInitializer(RobotManager manager);
}
