package cn.rookiex;

import cn.rookiex.coon.server.timer.TimerHolder;
import cn.rookiex.robot.ExampleRobotFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author rookieX 2022/12/5
 */
public class EchoServer {
    public static void main(String[] args)  throws Exception  {
        ExampleRobotFactory exampleRobotFactory = new ExampleRobotFactory();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);

        TimerHolder.init();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(exampleRobotFactory.getServerChannelInitializer());

            ChannelFuture f = b.bind(8090).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
