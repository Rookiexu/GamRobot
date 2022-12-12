package cn.rookiex.robot;

import cn.rookiex.client.Client;
import cn.rookiex.core.Message;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.manager.RobotConfig;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.observer.ObservedEvents;
import cn.rookiex.observer.ObservedParams;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author rookieX 2022/12/5
 */
@Data
@Log4j2
public class Robot {

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
     * 1 : pre
     * 2 : order
     * 3 : disorder
     */
    private int curStage;

    private int curModIdx;

    private int waitRespId;

    private int runTimes;

    private List<Module> randomModules;

    private int curEventIdx;

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

    public RobotManager getRobotManager() {
        return robotContext.getRobotManager();
    }

    public ModuleManager getModuleManager() {
        return getRobotManager().getModuleManager();
    }


    public void dealRespEvent() {
        Message poll = respQueue.poll();

        if (poll != null) {
            int id = poll.messageId();
            RespGameEvent respEvent = getRespEvent(id);
            if (respEvent == null) {
                log.error("消息号 : " + id + " ,不存在对应相应handler");
            } else {
                respEvent.dealResp(poll, this.robotContext);
                notify(ObservedEvents.INCR_RESP_DEAL, Maps.newHashMap());
            }

            if (waitRespId != 0 && poll.messageId() == waitRespId) {
                waitRespId = 0;
            }
        }
    }

    private void notify(String eventType, Map<String, Object> info) {
        try {
            info.put(ObservedParams.PROCESSOR_ID, getExecutorId());
            RobotProcessor processor = getRobotContext().getRobotManager().getProcessor(getExecutorId());
            processor.notify(eventType, info);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private RespGameEvent getRespEvent(int id) {
        ModuleManager moduleManager = this.robotContext.getRobotManager().getModuleManager();
        Map<Integer, RespGameEvent> respEventMap = moduleManager.getRespEventMap();
        return respEventMap.get(id);
    }

    public void dealSendEvent() {
        //TODO check 是否到处理时间
        //TODO check 是否需要等返回消息
        //TODO check 是否已经获得返回消息

        ReqGameEvent executeEvent = getExecuteEvent();
        if (executeEvent != null) {
            Map<String, Object> eventMap = Maps.newHashMap();
            executeEvent.dealReq(this.robotContext);

            boolean skip = this.robotContext.isSkip();
            if (skip) {
                this.waitRespId = 0;
                this.robotContext.resetSkip();
            } else {
                this.waitRespId = executeEvent.waitId();
            }
            eventMap.put(ObservedParams.WAIT_RESP_ID, waitRespId);
            eventMap.put(ObservedParams.IS_SKIP_RESP, skip);
            notify(ObservedEvents.INCR_SEND, eventMap);
        }
    }

    private ReqGameEvent getExecuteEvent() {
        int currentStage = getCurStage();

        List<Module> modules = null;
        switch (currentStage) {
            case Module.PRE:
                modules = getModuleManager().getPreModule();
                break;
            case Module.ORDER:
                modules = getModuleManager().getOrderModule();
                break;
            case Module.RANDOM:
                modules = this.randomModules;
            default:
                log.error("机器人执行过程,没有对应的执行阶段 : " + currentStage);
        }
        if (modules != null) {
            int curModIdx = getCurModIdx();
            int size = modules.size();
            if (curModIdx == size){

            }
            Module module = modules.get(curModIdx);
            //根据当前阶段,获得当前执行的mod
            Module currentMod = getCurrentMod();
            ReqGameEvent nextEvent = currentMod.getNextEvent();
            return nextEvent;
        }
        return null;
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
        return this.channel != null && channel.isActive();
    }

    public void setRandomModules(List<Module> randomModules) {
        this.randomModules = randomModules;
    }

    public List<Module> getRandomModules() {
        return randomModules;
    }

    public void setCurEventIdx(int curEventIdx) {
        this.curEventIdx = curEventIdx;
    }

    public int getCurEventIdx() {
        return curEventIdx;
    }
}
