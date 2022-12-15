package cn.rookiex.coon;

import cn.rookiex.client.Client;
import io.netty.channel.Channel;

/**
 * @author rookieX 2022/12/14
 */
public class RobotClient {
    public static void main(String[] args) throws Exception {
        Channel channel = Client.newChannel("127.0.0.1", 8090, new NioChannelInitializer());
        channel.writeAndFlush(new SimpleMessage(1,"1111"));

        System.out.println(channel.isActive());
    }
}
