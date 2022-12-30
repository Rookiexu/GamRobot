package cn.rookiex.record;

import cn.rookiex.observer.observed.ObservedParams;
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
public class WindowRecord {

    private Map<Integer, ProcessorRecord> processorRecordMap = Maps.newHashMap();

    private Set<Integer> processorIds;

    public void init(Set<Integer> processorIds) {
        this.processorIds = processorIds;
        init0();
    }

    private void init0() {
        for (Integer processorId : this.processorIds) {
            processorRecordMap.put(processorId, new ProcessorRecord());
        }
    }

    public ProcessorRecord getProcessorRecord(int id) {
        ProcessorRecord processorRecord = processorRecordMap.get(id);
        if (processorRecord == null) {
            log.error("压测机器人执行线程不存在 : " + id, new Throwable());
        }
        return processorRecord;
    }

    public void dealRespDeal(Integer id, Map<String, Object> info) {
        incrProcessorLong(ProcessorRecord::getTotalRespDeal, id);

        //处理返回消息
        ProcessorRecord processorRecord = getProcessorRecord(id);
        int waitId = (int) info.get(ObservedParams.WAIT_RESP_ID);
        AtomicInteger old = processorRecord.getWaitMsg().get(waitId);
        if (old != null) {
            old.decrementAndGet();
        }

        //响应耗时记录
        long respCost = (long) info.get(ObservedParams.RESP_COST);
        processorRecord.getRespCost().addCost((int) respCost);
    }


    public void dealSend(Integer id, Map<String, Object> info) {
        incrProcessorLong(ProcessorRecord::getTotalSend, id);

        ProcessorRecord processorRecord = getProcessorRecord(id);
        boolean isSkip = (boolean) info.get(ObservedParams.IS_SKIP_RESP);
        if (!isSkip) {
            int waitId = (int) info.get(ObservedParams.WAIT_RESP_ID);
            AtomicInteger wait = processorRecord.getWaitMsg().putIfAbsent(waitId, new AtomicInteger());
            if (wait == null) {
                wait = processorRecord.getWaitMsg().get(waitId);
            }
            AtomicInteger send = processorRecord.getSendMsg().putIfAbsent(waitId, new AtomicInteger());
            if (send == null) {
                send = processorRecord.getSendMsg().get(waitId);
            }
            wait.incrementAndGet();
            send.incrementAndGet();
        }
    }

    public int getTotalInt(Function<ProcessorRecord, AtomicInteger> function) {
        int total = 0;
        for (ProcessorRecord value : getProcessorRecordMap().values()) {
            AtomicInteger apply = function.apply(value);
            total += apply.get();
        }
        return total;
    }

    public long getTotalLong(Function<ProcessorRecord, AtomicLong> function) {
        long total = 0;
        for (ProcessorRecord value : getProcessorRecordMap().values()) {
            AtomicLong apply = function.apply(value);
            total += apply.get();
        }
        return total;
    }


    public void incrProcessorInt(Function<ProcessorRecord, AtomicInteger> function, int id) {
        ProcessorRecord processorRecord = getProcessorRecord(id);
        function.apply(processorRecord).incrementAndGet();
    }

    public void incrProcessorLong(Function<ProcessorRecord, AtomicLong> function, int id) {
        ProcessorRecord processorRecord = getProcessorRecord(id);
        function.apply(processorRecord).incrementAndGet();
    }

    public void clear() {
        getProcessorRecordMap().clear();
        init0();
    }

    public String getLogInfo(){
        StringBuilder builder = new StringBuilder();

        builder.append("总机器人").append(" : ").append(getTotalInt(ProcessorRecord::getTotalRobot)).append(", ");
        builder.append("总发送消息").append(" : ").append(getTotalLong(ProcessorRecord::getTotalSend)).append(", ");
        builder.append("总接收消息").append(" : ").append(getTotalLong(ProcessorRecord::getTotalRespDeal)).append(", ");

        RespondBucket respondBucket = mergeRespondBucket();
        builder.append("平均响应时间").append(" : ").append(respondBucket.getAvgResp()).append("ms, ");
        builder.append("99.99%响应时").append(" : ").append(respondBucket.getRespTime(9999)).append("ms, ");
        builder.append("99.9%响应时").append(" : ").append(respondBucket.getRespTime(9990)).append("ms, ");
        builder.append("99%响应时").append(" : ").append(respondBucket.getRespTime(9900)).append(", ");
        builder.append("200ms响应数").append(" : ").append(respondBucket.getSlowRespCount());



        return builder.toString();
    }

    public void sum(WindowRecord record) {
        Map<Integer, ProcessorRecord> processorRecordMap = record.getProcessorRecordMap();
        for (Integer id : processorRecordMap.keySet()) {
            ProcessorRecord from = processorRecordMap.get(id);
            ProcessorRecord to = getProcessorRecordMap().get(id);
            if (to == null){
                to = new ProcessorRecord();
                getProcessorRecordMap().put(id, to);
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
        Collection<ProcessorRecord> values = getProcessorRecordMap().values();
        for (ProcessorRecord value : values) {
            mergeInteger(value.getRespCost().getCostBucket(), respondBucket.getCostBucket());
        }
        return respondBucket;
    }
}
