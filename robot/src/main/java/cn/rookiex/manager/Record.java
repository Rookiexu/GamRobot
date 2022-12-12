package cn.rookiex.manager;

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
                dealIncrCoon(info);
                break;
            case ObservedEvents.INCR_LOGIN:
                dealIncrLogin(info);
                break;
            case ObservedEvents.INCR_SEND:
                dealIncrSend(info);
                break;
            case ObservedEvents.INCR_RESP:
                dealIncrResp(info);
                break;
            case ObservedEvents.INCR_RESP_DEAL:
                dealIncrRespDeal(info);
                break;
            case ObservedEvents.INCR_ROBOT:
                dealIncrRobot(info);
                break;
        }
    }

    private void dealIncrRobot(Map<String, Object> info) {
        Integer id = (Integer) info.get(ObservedParams.PROCESSOR_ID);
        getProcessorRecord(id).getTotalRobot().incrementAndGet();
    }

    private void dealIncrRespDeal(Map<String, Object> info) {
        Integer id = (Integer) info.get(ObservedParams.PROCESSOR_ID);
        getProcessorRecord(id).getTotalRespDeal().incrementAndGet();
    }

    private void dealIncrResp(Map<String, Object> info) {
        Integer id = (Integer) info.get(ObservedParams.PROCESSOR_ID);
        getProcessorRecord(id).getTotalResp().incrementAndGet();
    }

    private void dealIncrSend(Map<String, Object> info) {
        Integer id = (Integer) info.get(ObservedParams.PROCESSOR_ID);
        getProcessorRecord(id).getTotalSend().incrementAndGet();
    }

    private void dealIncrLogin(Map<String, Object> info) {
        Integer id = (Integer) info.get(ObservedParams.PROCESSOR_ID);
        getProcessorRecord(id).getTotalLogin().incrementAndGet();
    }

    private void dealIncrCoon(Map<String, Object> info) {
        Integer id = (Integer) info.get(ObservedParams.PROCESSOR_ID);
        getProcessorRecord(id).getTotalCoon().incrementAndGet();
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
}
