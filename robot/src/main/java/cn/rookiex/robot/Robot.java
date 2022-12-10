package cn.rookiex.robot;

import cn.rookiex.client.Client;
import cn.rookiex.core.Message;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.manager.RobotConfig;
import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
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
    private Channel channel;

    /**
     * 响应事件队列
     */
    private Queue<Message> respQueue = new LinkedBlockingQueue<Message>();

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

    /**
     * 0 : pre
     * 1 : order
     * 3 : disorder
     *
     */
    private int currentStage;

    private int waitRespId;

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
        Message poll = respQueue.poll();

        if (poll != null){
            int id = poll.messageId();
            RespGameEvent respEvent = getRespEvent(id);
            if (respEvent == null){
                log.error("消息号 : " + id + " ,不存在对应相应handler");
            }else {
                respEvent.dealResp(poll, this.robotContext);
            }

            if (waitRespId != 0 && poll.messageId() == waitRespId){
                waitRespId = 0;
            }
        }
    }

    private RespGameEvent getRespEvent(int id) {
        ModuleManager moduleManager = this.robotContext.getRobotManager().getModuleManager();
        Map<Integer, RespGameEvent> respEventMap = moduleManager.getRespEventMap();
        return respEventMap.get(id);
    }

    public void dealSendEvent() {
        //check 是否到处理时间
        //check 是否需要等返回消息
            //check 是否已经获得返回消息

        ReqGameEvent executeEvent = getExecuteEvent();
        if (executeEvent != null) {
            executeEvent.dealReq(this.robotContext);
        }
    }

    private ReqGameEvent getExecuteEvent() {
        //ai module 和 普通的order module 需要有不同的分支
        int currentStage = getCurrentStage();
        //根据当前阶段,获得当前执行的mod
        Module currentMod = getCurrentMod();
        ReqGameEvent nextEvent = currentMod.getNextEvent();
        return nextEvent;
    }

    public void connect() throws Exception {
        RobotConfig config = getRobotConfig();
        String serverIp = config.getServerIp();
        int serverPort = config.getServerPort();

        this.channel = Client.newChannel(serverIp, serverPort);
    }

    private RobotConfig getRobotConfig() {
        return this.robotContext.getRobotManager().getConfig();
    }

    public boolean isConnect() {
        return this.channel != null && channel.isActive() ;
    }
}
