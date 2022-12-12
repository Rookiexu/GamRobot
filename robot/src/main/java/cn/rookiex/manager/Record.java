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
        Integer id = (Integer) info.get(ObservedParams.PROCESSOR_ID);
        switch (message){
            case ObservedEvents.INCR_COON:
                incrProcessorInt(id, ProcessorRecord::getTotalCoon);
                break;
            case ObservedEvents.INCR_LOGIN:
                incrProcessorInt(id, ProcessorRecord::getTotalLogin);
                break;
            case ObservedEvents.INCR_SEND:
                dealSend(id, info);
                break;
            case ObservedEvents.INCR_RESP:
                incrProcessorLong(id, ProcessorRecord::getTotalResp);
                break;
            case ObservedEvents.INCR_RESP_DEAL:
                incrProcessorLong(id, ProcessorRecord::getTotalRespDeal);
                break;
            case ObservedEvents.INCR_ROBOT:
                incrProcessorInt(id, ProcessorRecord::getTotalRobot);
                break;
        }
    }

    private void dealSend(Integer id, Map<String, Object> info) {
        incrProcessorLong(id, ProcessorRecord::getTotalSend);

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


    public void incrProcessorInt(int id, Function<ProcessorRecord, AtomicInteger> function){
        ProcessorRecord processorRecord = getProcessorRecord(id);
        function.apply(processorRecord).incrementAndGet();
    }

    public void incrProcessorLong(int id, Function<ProcessorRecord, AtomicLong> function){
        ProcessorRecord processorRecord = getProcessorRecord(id);
        function.apply(processorRecord).incrementAndGet();
    }
}
