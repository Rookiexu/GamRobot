package cn.rookiex.record;

import cn.rookiex.observer.ObservedEvents;
import cn.rookiex.observer.ObservedParams;
import cn.rookiex.observer.Observer;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author rookieX 2022/12/9
 */
@Getter
@Log4j2
public class Record implements Observer {

    private long logTime;

    private Map<Integer,ProcessorRecord> processorRecordMap = Maps.newHashMap();

    public ProcessorRecord getProcessorRecord(int id){
        ProcessorRecord processorRecord = processorRecordMap.get(id);
        if (processorRecord == null){
            log.error("压测机器人执行线程不存在 : " + id, new Throwable());
        }
        return processorRecord;
    }

    @Override
    public void update(String message, Map<String, Object> info) {
        switch (message){
            case ObservedEvents.INCR_COON:
                incrProcessorInt(ProcessorRecord::getTotalCoon, (Integer) info.get(ObservedParams.PROCESSOR_ID));
                break;
            case ObservedEvents.INCR_SEND:
                dealSend((Integer) info.get(ObservedParams.PROCESSOR_ID), info);
                dealSpecialMsg(info);
                break;
            case ObservedEvents.INCR_RESP:
                incrProcessorLong(ProcessorRecord::getTotalResp, (Integer) info.get(ObservedParams.PROCESSOR_ID));
                break;
            case ObservedEvents.INCR_RESP_DEAL:
                incrProcessorLong(ProcessorRecord::getTotalRespDeal, (Integer) info.get(ObservedParams.PROCESSOR_ID));
                break;
            case ObservedEvents.INCR_ROBOT:
                incrProcessorInt(ProcessorRecord::getTotalRobot, (Integer) info.get(ObservedParams.PROCESSOR_ID));
                break;
            case ObservedEvents.TICK_TIME:
                dealTick(info);
                break;
        }
    }

    private void dealSpecialMsg(Map<String, Object> info) {
        //todo 扩展对特殊消息的处理,比如登录等
    }

    private void dealTick(Map<String, Object> info) {
        long cur = (long) info.get(ObservedParams.CUR_MS);
        if (cur - logTime > 5000) {
            log.info("压测执行进度 -----------------------------");
            for (Integer id : processorRecordMap.keySet()) {
                ProcessorRecord processorRecord = processorRecordMap.get(id);
                String s = processorRecord.toString();
                log.info("执行器 : " + id + " ,执行情况 : " + s);
            }
        }
    }

    private void dealSend(Integer id, Map<String, Object> info) {
        incrProcessorLong(ProcessorRecord::getTotalSend, id);

        ProcessorRecord processorRecord = getProcessorRecord(id);
        boolean isSkip = (boolean) info.get(ObservedParams.IS_SKIP_RESP);
        if (!isSkip){
            int waitId = (int) info.get(ObservedParams.WAIT_RESP_ID);
            AtomicInteger old = processorRecord.getWaitMsg().putIfAbsent(waitId, new AtomicInteger());
            if (old == null){
                old = processorRecord.getWaitMsg().get(waitId);
            }
            old.incrementAndGet();
        }
    }

    public int getTotalInt(Function<ProcessorRecord, AtomicInteger> function){
        int total = 0;
        for (ProcessorRecord value : getProcessorRecordMap().values()) {
            AtomicInteger apply = function.apply(value);
            total += apply.get();
        }
        return total;
    }

    public long getTotalLong(Function<ProcessorRecord, AtomicLong> function){
        long total = 0;
        for (ProcessorRecord value : getProcessorRecordMap().values()) {
            AtomicLong apply = function.apply(value);
            total += apply.get();
        }
        return total;
    }


    public void incrProcessorInt(Function<ProcessorRecord, AtomicInteger> function, int id){
        ProcessorRecord processorRecord = getProcessorRecord(id);
        function.apply(processorRecord).incrementAndGet();
    }

    public void incrProcessorLong(Function<ProcessorRecord, AtomicLong> function, int id){
        ProcessorRecord processorRecord = getProcessorRecord(id);
        function.apply(processorRecord).incrementAndGet();
    }
}
