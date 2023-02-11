package cn.rookiex.robot;

import cn.rookiex.message.Message;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.event.SkipEvent;
import cn.rookiex.config.RobotConfig;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.module.mod.Module;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.module.stage.ModuleStage;
import cn.rookiex.module.stage.RunModule;
import cn.rookiex.robot.ctx.RobotContext;
import cn.rookiex.sentinel.record.info.MsgInfo;
import cn.rookiex.sentinel.pubsub.cons.SystemEventsKeys;
import cn.rookiex.sentinel.pubsub.cons.SystemEventParams;
import cn.rookiex.sentinel.pubsub.SystemEvent;
import cn.rookiex.sentinel.pubsub.SystemEventImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
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


    public static final AttributeKey<String> CHANNEL_ATTR_ID = AttributeKey.valueOf("robotId");

    /**
     * 机器人id
     */
    private String id;

    /**
     * 机器人名字
     */
    private String simpleName;

    /**
     * 机器人全名
     */
    private String fullName;

    /**
     * 服务器连接
     */
    private Channel channel;

    /**
     * 响应事件队列
     */
    private Queue<Message> respQueue = new LinkedBlockingQueue<Message>();

    /**
     * 线程池id
     */
    private int executorId;

    /**
     * 执行上下文
     */
    private RobotContext robotContext;

    /**
     * 执行状态模式
     */
    private ModuleStage runStage = new RunModule();

    /**
     * 1 : pre
     * 2 : order
     * 3 : disorder
     */
    private int curModStage;

    /**
     * 当前mod
     */
    private int curModIdx;

    /**
     * mod执行次数,0的时候子mod需要
     */
    private int modRunTimes;

    /**
     * 等待响应消息
     */
    private int waitRespId;

    private List<Module> randomModules;

    private int curEventIdx;

    private List<ReqGameEvent> curEventList;

    private long reqSendTime;

    private ChannelInitializer channelInitializer;

    private GameRobot gameRobot;


    public void setId(String id) {
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

            int id = poll.getMsgId();
            RespGameEvent respEvent = getRespEvent(id);
            if (respEvent == null) {
                log.error("消息号 : " + id + " ,不存在对应相应handler");
            } else {
                respEvent.dealResp(poll, this.robotContext);

                dealRespEvent(poll, respEvent);
            }

            if (waitRespId != 0 && poll.getMsgId() == waitRespId) {
                setWaitRespId(0);
            }
        }
    }

    private void dealRespEvent(Message poll, RespGameEvent respEvent) {
        SystemEvent systemEvent = new SystemEventImpl(SystemEventsKeys.INCR_RESP_DEAL);
        systemEvent.put(SystemEventParams.WAIT_RESP_ID, respEvent.eventId());
        systemEvent.put(SystemEventParams.ROBOT_ID, getFullName());
        if (poll instanceof MsgInfo){
            long createTime = ((MsgInfo) poll).getCreateTime();
            systemEvent.put(SystemEventParams.RESP_TIME, createTime);
            if (waitRespId != 0 && poll.getMsgId() == waitRespId) {
                long reqSendTime = getReqSendTime();
                systemEvent.put(SystemEventParams.RESP_COST, createTime - reqSendTime);
                systemEvent.put(SystemEventParams.RESP_DEAL_COST, System.currentTimeMillis() - createTime);
            }
        }
        notify0(systemEvent);
    }

    private void notify0(SystemEvent systemEvent) {
        systemEvent.put(SystemEventParams.PROCESSOR_ID, getExecutorId());
        getRobotManager().getRecordProcessor().publish(systemEvent);
    }

    private RespGameEvent getRespEvent(int id) {
        ModuleManager moduleManager = this.robotContext.getRobotManager().getModuleManager();
        Map<Integer, RespGameEvent> respEventMap = moduleManager.getRespEventMap();
        return respEventMap.get(id);
    }

    public void dealSendEvent() {
        if (needWait()) {
            return;
        }

        ReqGameEvent executeEvent = getExecuteEvent();
        setReqSendTime(System.currentTimeMillis());
        if (executeEvent != null) {
            if (!(executeEvent instanceof SkipEvent)){
                dealReq0(executeEvent);
            }
        }else {
            log.error("阶段获取执行event为空 stage : " + runStage.toString());
        }
    }

    private boolean needWait() {
        //check 是否到处理时间

        long l = System.currentTimeMillis();
        if (getReqSendTime() > l - getRobotConfig().getReqIntervalTime()){
            return true;
        }

        //check 是否需要等返回消息
        //check 是否已经获得返回消息
        if (robotContext.isSkip()) {
            robotContext.resetSkip();
        } else {
            return waitRespId != 0;
        }
        return false;
    }

    private void dealReq0(ReqGameEvent executeEvent) {
        executeEvent.dealReq(this.robotContext);

        boolean skip = this.robotContext.isSkip();
        if (skip) {
            this.waitRespId = 0;
            this.robotContext.resetSkip();
        } else {
            this.waitRespId = executeEvent.waitId();
        }

        SystemEvent systemEvent = new SystemEventImpl(SystemEventsKeys.INCR_SEND);
        systemEvent.put(SystemEventParams.WAIT_RESP_ID, waitRespId);
        systemEvent.put(SystemEventParams.ROBOT_ID, getFullName());
        systemEvent.put(SystemEventParams.REQ_MSG_ID, executeEvent.eventId());
        systemEvent.put(SystemEventParams.REQ_MSG_NAME, executeEvent.getClass().getSimpleName());
        systemEvent.put(SystemEventParams.IS_SKIP_RESP, skip);
        notify0(systemEvent);
    }

    private ReqGameEvent getExecuteEvent() {
        //当前stage
        Integer curDeep = 0;
        runStage = tryNextStage(runStage, curDeep, 10);

        boolean modOver = runStage.isModOver(robotContext);
        if (modOver) {
            runStage.toNextMod(robotContext);
            runStage.initMod(robotContext);
        }

        return runStage.getEvent(robotContext);
    }

    private ModuleStage tryNextStage(ModuleStage stage, Integer curDeep, int maxDeep){
        if (maxDeep < 0 || maxDeep > 999){
            maxDeep = 999;
        }

        if (curDeep >= maxDeep){
            return stage;
        }else {
            curDeep++;
        }

        if (stage.isStageOver(robotContext)){
            stage = stage.nextStage(robotContext);
            stage.initStage(robotContext);
            return tryNextStage(stage, curDeep, maxDeep);
        }

        return stage;
    }


    public void connect() throws Exception {
        RobotConfig config = getRobotConfig();
        String serverIp = config.getServerIp();
        int serverPort = config.getServerPort();

        this.channel = newChannel(serverIp, serverPort);
        channel.attr(CHANNEL_ATTR_ID).set(this.id);
        runStage.initStage(robotContext);
    }

    private Channel newChannel(String serverIp, int serverPort) throws InterruptedException {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(getChannelInitializer());

        // Start the client.
        return b.connect(serverIp, serverPort).sync().channel();
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

    public int getCurEventIdx() {
        return curEventIdx;
    }

    public void setCurEventIdx(int curEventIdx) {
        this.curEventIdx = curEventIdx;
    }

    public void setModEventList(List<ReqGameEvent> curEventList) {
        this.curEventList = curEventList;
    }

    public List<ReqGameEvent> getCurEventList() {
        return curEventList;
    }

    public long getReqSendTime() {
        return reqSendTime;
    }

    public void setReqSendTime(long reqSendTime) {
        this.reqSendTime = reqSendTime;
    }

    public void setChannelInitializer(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public ChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }
}
