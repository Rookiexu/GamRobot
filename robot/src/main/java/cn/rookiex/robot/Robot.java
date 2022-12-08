package cn.rookiex.robot;

import cn.rookiex.client.Client;
import cn.rookiex.event.RespEvent;
import cn.rookiex.module.Module;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author rookieX 2022/12/5
 */
@Data
@Log4j2
public class Robot{

    /**
     * 服务器连接
     */
    private ChannelFuture channel;

    /**
     * 响应事件队列
     */
    private Queue<RespEvent> respQueue = new LinkedBlockingQueue<RespEvent>();

    /**
     * 机器人id
     */
    private long id;

    /**
     * 机器人名字
     */
    private String simpleName;

    /**
     * 机器人全名
     */
    private String fullName;

    /**
     * 线程池id
     */
    private int executorId;

    /**
     * 执行上下文
     */
    private RobotContext robotContext;

    private Module currentMod;

    private int waitRespId;


    public void initChannel(String ip, int port) throws Exception {
        this.channel = Client.newChannel(ip, port);
    }

    public void setId(long id) {
        this.id = id;
        this.fullName = simpleName + "_" + id;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
        this.fullName = simpleName + "_" + id;
    }

    public void setExecutorId(int executorId) {
        this.executorId = executorId;
    }

    public int getExecutorId() {
        return executorId;
    }


    public void setRobotContext(RobotContext robotContext) {
        this.robotContext = robotContext;
    }

    public RobotContext getRobotContext() {
        return robotContext;
    }

    public void dealRespEvent() {
        RespEvent poll = respQueue.poll();
        if (poll != null){
            poll.dealResp(this.robotContext);
            if (waitRespId != 0 && poll.eventId() == waitRespId){
                waitRespId = 0;
            }
        }
    }

    public void dealSendEvent() {
        Module currentMod = getCurrentMod();
        if (currentMod == null){

        }
    }
}
