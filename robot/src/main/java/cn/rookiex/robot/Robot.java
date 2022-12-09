package cn.rookiex.robot;

import cn.rookiex.client.Client;
import cn.rookiex.core.Message;
import cn.rookiex.event.ReqEvent;
import cn.rookiex.event.RespEvent;
import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import io.netty.channel.ChannelFuture;
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
    private ChannelFuture channel;

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
        Message poll = respQueue.poll();

        if (poll != null){
            int id = poll.messageId();
            RespEvent respEvent = getRespEvent(id);
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

    private RespEvent getRespEvent(int id) {
        ModuleManager moduleManager = this.robotContext.getRobotManager().getModuleManager();
        Map<Integer, RespEvent> respEventMap = moduleManager.getRespEventMap();
        return respEventMap.get(id);
    }

    public void dealSendEvent() {
        ReqEvent executeEvent = getExecuteEvent();
        if (executeEvent != null) {
            executeEvent.dealReq(this.robotContext);
        }
    }

    private ReqEvent getExecuteEvent() {
        int currentStage = getCurrentStage();
        //根据当前阶段,获得当前执行的mod
        Module currentMod = getCurrentMod();
        ReqEvent nextEvent = currentMod.getNextEvent();
        return nextEvent;
    }
}
