package cn.rookiex.robot;

import cn.rookiex.client.Client;
import cn.rookiex.event.ReqEvent;
import cn.rookiex.event.RespEvent;
import io.netty.channel.ChannelFuture;
import lombok.Data;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author rookieX 2022/12/5
 */
@Data
public class Robot {

    /**
     * 服务器连接
     */
    private ChannelFuture channel;

    /**
     * 加载事件队列,登录初始化等等,整个机器人流程只执行一次
     */
    private Queue<ReqEvent> loadQueue = new LinkedBlockingQueue<ReqEvent>();

    /**
     * 请求事件队列
     */
    private Queue<ReqEvent> reqQueue = new LinkedBlockingQueue<ReqEvent>();

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


    public void initChannel(ChannelFuture channel){
        this.channel = channel;
    }

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
}
