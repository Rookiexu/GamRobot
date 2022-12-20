package cn.rookiex.record;

import cn.rookiex.observer.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author rookieX 2022/12/9
 */
@Getter
@Log4j2
public class Record implements Observer, Observable {

    private long logTime;

    private Map<Integer,ProcessorRecord> processorRecordMap = Maps.newHashMap();

    private List<Observer> observableList = Lists.newCopyOnWriteArrayList();

    public ProcessorRecord getProcessorRecord(int id){
        ProcessorRecord processorRecord = processorRecordMap.get(id);
        if (processorRecord == null){
            log.error("压测机器人执行线程不存在 : " + id, new Throwable());
        }
        return processorRecord;
    }

    @Override
    public void update(UpdateEvent message) {
        switch (message.getKey()){
            case ObservedEvents.INCR_COON:
                incrProcessorInt(ProcessorRecord::getTotalCoon, (Integer) message.get(ObservedParams.PROCESSOR_ID));
                break;
            case ObservedEvents.INCR_SEND:
                dealSend((Integer) message.get(ObservedParams.PROCESSOR_ID), message);
                dealSpecialMsg(message);
                break;
            case ObservedEvents.INCR_RESP:
                incrProcessorLong(ProcessorRecord::getTotalResp, (Integer) message.get(ObservedParams.PROCESSOR_ID));
                break;
            case ObservedEvents.INCR_RESP_DEAL:
                dealRespDeal((Integer) message.get(ObservedParams.PROCESSOR_ID), message);
                break;
            case ObservedEvents.INCR_ROBOT:
                incrProcessorInt(ProcessorRecord::getTotalRobot, (Integer) message.get(ObservedParams.PROCESSOR_ID));
                break;
            case ObservedEvents.TICK_TIME:
                dealTick(message);
                break;
        }

        notify(message);
    }

    private void dealRespDeal(Integer id, Map<String, Object> info) {
        incrProcessorLong(ProcessorRecord::getTotalRespDeal, id);

        ProcessorRecord processorRecord = getProcessorRecord(id);
        int waitId = (int) info.get(ObservedParams.WAIT_RESP_ID);
        AtomicInteger old = processorRecord.getWaitMsg().get(waitId);
        if (old != null){
            old.decrementAndGet();
        }
    }

    private void dealSpecialMsg(Map<String, Object> info) {
        //todo 扩展对特殊消息的处理,比如登录等
    }

    private void dealTick(Map<String, Object> info) {
        long cur = (long) info.get(ObservedParams.CUR_MS);
        if (cur - logTime > 5000) {
            logTime = cur;
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
            AtomicInteger wait = processorRecord.getWaitMsg().putIfAbsent(waitId, new AtomicInteger());
            if (wait == null){
                wait = processorRecord.getWaitMsg().get(waitId);
            }
            AtomicInteger send = processorRecord.getSendMsg().putIfAbsent(waitId, new AtomicInteger());
            if (send == null){
                send = processorRecord.getSendMsg().get(waitId);
            }
            wait.incrementAndGet();
            send.incrementAndGet();
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

    @Override
    public void register(Observer o) {
        observableList.add(o);
    }

    @Override
    public void remove(Observer o) {
        observableList.remove(o);
    }

    @Override
    public void notify(UpdateEvent message) {
        for (Observer observer : observableList) {
            observer.update(message);
        }
    }
}
