package cn.rookiex.robot;

import cn.rookiex.coon.client.initializer.ClientMultiChannelInitializer;
import cn.rookiex.coon.server.initializer.ServerMultiChannelInitializer;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.ctx.RobotContext;
import cn.rookiex.robot.ctx.RobotCtx;
import io.netty.channel.ChannelInitializer;

/**
 * @author rookieX 2022/12/8
 */
public class ExampleRobotFactory implements RobotFactory {

    private static ChannelInitializer clientInitializer = new ClientMultiChannelInitializer();
//    ChannelInitializer initializer = new ClientStrChannelInitializer();

    private static ChannelInitializer serverInitializer = new ServerMultiChannelInitializer();

    @Override
    public Robot newRobot(RobotManager manager) {
        return new Robot();
    }

    @Override
    public RobotContext newRobotContext(RobotManager manager) {
        return new RobotCtx();
    }

    @Override
    public ChannelInitializer getClientChannelInitializer() {
        return clientInitializer;
    }

    @Override
    public ChannelInitializer getServerChannelInitializer() {
        return serverInitializer;
    }

}
