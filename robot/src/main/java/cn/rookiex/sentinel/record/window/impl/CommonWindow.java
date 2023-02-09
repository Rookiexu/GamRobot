package cn.rookiex.sentinel.record.window.impl;

import cn.rookiex.sentinel.pubsub.cons.SystemEventParams;
import cn.rookiex.sentinel.record.info.ProcessorInfo;
import cn.rookiex.sentinel.record.info.RespondBucket;
import cn.rookiex.sentinel.record.window.Window;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author rookieX 2022/12/28
 */
@Log4j2
@Getter
public class CommonWindow implements Window {

    private Map<Integer, ProcessorInfo> ProcessorInfoMap = Maps.newHashMap();

    private Set<Integer> processorIds;

    @Override
    public void init() {
        for (Integer processorId : this.processorIds) {
            ProcessorInfoMap.put(processorId, new ProcessorInfo());
        }
    }

    public ProcessorInfo getProcessorInfo(int id) {
        ProcessorInfo ProcessorInfo = ProcessorInfoMap.get(id);
        if (ProcessorInfo == null) {
            log.error("压测机器人执行线程不存在 : " + id, new Throwable());
        }
        return ProcessorInfo;
    }

    public void dealRespDeal(Integer id, Map<String, Object> info) {
        incrProcessorLong(ProcessorInfo::getTotalRespDeal, id);

        //处理返回消息
        ProcessorInfo ProcessorInfo = getProcessorInfo(id);
        int waitId = (int) info.get(SystemEventParams.WAIT_RESP_ID);
        AtomicInteger old = ProcessorInfo.getWaitMsg().get(waitId);
        if (old != null) {
            old.decrementAndGet();
        }

        //响应耗时记录
        long respCost = (long) info.get(SystemEventParams.RESP_COST);
        ProcessorInfo.getRespCost().addCost((int) respCost);
    }


    public void dealSend(Integer id, Map<String, Object> info) {
        incrProcessorLong(ProcessorInfo::getTotalSend, id);

        ProcessorInfo ProcessorInfo = getProcessorInfo(id);
        boolean isSkip = (boolean) info.get(SystemEventParams.IS_SKIP_RESP);
        if (!isSkip) {
            int waitId = (int) info.get(SystemEventParams.WAIT_RESP_ID);
            AtomicInteger wait = ProcessorInfo.getWaitMsg().putIfAbsent(waitId, new AtomicInteger());
            if (wait == null) {
                wait = ProcessorInfo.getWaitMsg().get(waitId);
            }
            AtomicInteger send = ProcessorInfo.getSendMsg().putIfAbsent(waitId, new AtomicInteger());
            if (send == null) {
                send = ProcessorInfo.getSendMsg().get(waitId);
            }
            wait.incrementAndGet();
            send.incrementAndGet();
        }
    }

    public int getTotalRobot(){
        int total = 0;
        for (ProcessorInfo value : getProcessorInfoMap().values()) {
            int size = value.getRobotName().size();
            total += size;
        }
        return total;
    }

    public int getTotalInt(Function<ProcessorInfo, AtomicInteger> function) {
        int total = 0;
        for (ProcessorInfo value : getProcessorInfoMap().values()) {
            AtomicInteger apply = function.apply(value);
            total += apply.get();
        }
        return total;
    }

    public long getTotalLong(Function<ProcessorInfo, AtomicLong> function) {
        long total = 0;
        for (ProcessorInfo value : getProcessorInfoMap().values()) {
            AtomicLong apply = function.apply(value);
            total += apply.get();
        }
        return total;
    }

    public void addActRobot(int id, String robotName){
        ProcessorInfo ProcessorInfo = getProcessorInfo(id);
        ProcessorInfo.getRobotName().add(robotName);
    }



    public void incrProcessorInt(Function<ProcessorInfo, AtomicInteger> function, int id) {
        ProcessorInfo ProcessorInfo = getProcessorInfo(id);
        function.apply(ProcessorInfo).incrementAndGet();
    }

    public void incrProcessorLong(Function<ProcessorInfo, AtomicLong> function, int id) {
        ProcessorInfo ProcessorInfo = getProcessorInfo(id);
        function.apply(ProcessorInfo).incrementAndGet();
    }

    @Override
    public void clear() {
        getProcessorInfoMap().clear();
        init();
    }

    @Override
    public String getLogInfo(){
        StringBuilder builder = new StringBuilder();

        builder.append("总机器人").append(" : ").append(getTotalRobot()).append(", ");
        builder.append("总发送消息").append(" : ").append(getTotalLong(ProcessorInfo::getTotalSend)).append(", ");
        builder.append("总接收消息").append(" : ").append(getTotalLong(ProcessorInfo::getTotalRespDeal)).append(", ");

        RespondBucket respondBucket = mergeRespondBucket();
        builder.append("平均响应时间").append(" : ").append(respondBucket.getAvgResp()).append("ms, ");
        builder.append("99.99%响应时").append(" : ").append(respondBucket.getRespTime(9999)).append("ms, ");
        builder.append("99.9%响应时").append(" : ").append(respondBucket.getRespTime(9990)).append("ms, ");
        builder.append("99%响应时").append(" : ").append(respondBucket.getRespTime(9900)).append(", ");
        builder.append("200ms响应数").append(" : ").append(respondBucket.getSlowRespCount());

        return builder.toString();
    }

    public void sum(CommonWindow record) {
        Map<Integer, ProcessorInfo> ProcessorInfoMap = record.getProcessorInfoMap();
        for (Integer id : ProcessorInfoMap.keySet()) {
            ProcessorInfo from = ProcessorInfoMap.get(id);
            ProcessorInfo to = getProcessorInfoMap().get(id);
            if (to == null){
                to = new ProcessorInfo();
                getProcessorInfoMap().put(id, to);
            }

            merge(from.getSendMsg(), to.getSendMsg());
            merge(from.getWaitMsg(), to.getWaitMsg());
            mergeInteger(from.getRespCost().getCostBucket(), to.getRespCost().getCostBucket());
            to.getTotalCoon().addAndGet(from.getTotalCoon().get());
            to.getTotalLogin().addAndGet(from.getTotalLogin().get());
            to.getTotalRobot().addAndGet(from.getTotalRobot().get());
            to.getTotalResp().addAndGet(from.getTotalResp().get());
            to.getTotalRespDeal().addAndGet(from.getTotalRespDeal().get());
            to.getTotalSend().addAndGet(from.getTotalSend().get());
            to.getRobotName().addAll(from.getRobotName());
        }
    }

    private void merge(Map<Integer, AtomicInteger> from, Map<Integer, AtomicInteger> to){
        for (Integer id : from.keySet()) {
            to.merge(id, from.get(id), (old, new0) ->{
                old.addAndGet(new0.get());
                return old;
            });
        }
    }

    private void mergeInteger(Map<Integer, Integer> from, Map<Integer, Integer> to){
        for (Integer id : from.keySet()) {
            to.merge(id, from.get(id), Integer::sum);
        }
    }


    private RespondBucket mergeRespondBucket(){
        RespondBucket respondBucket = new RespondBucket();
        Collection<ProcessorInfo> values = getProcessorInfoMap().values();
        for (ProcessorInfo value : values) {
            mergeInteger(value.getRespCost().getCostBucket(), respondBucket.getCostBucket());
        }
        return respondBucket;
    }

    public void setProcessorIds(Set<Integer> processorIds) {
        this.processorIds = processorIds;
    }
}
