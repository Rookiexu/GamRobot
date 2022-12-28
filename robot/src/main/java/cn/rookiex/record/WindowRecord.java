package cn.rookiex.record;

import cn.rookiex.observer.observed.ObservedParams;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

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
        int respCost = (int) info.get(ObservedParams.RESP_COST);
        processorRecord.getRespCost().addCost(respCost);
    }

    private void dealSpecialMsg(Map<String, Object> info) {
        //todo 扩展对特殊消息的处理,比如登录等
    }

    public void dealTick(Map<String, Object> info) {
        long cur = (long) info.get(ObservedParams.CUR_MS);
        log.info("压测执行进度 -----------------------------");
        for (Integer id : processorRecordMap.keySet()) {
            ProcessorRecord processorRecord = processorRecordMap.get(id);
            String s = processorRecord.toString();
            log.info("执行器 : " + id + " ,执行情况 : " + s);
        }
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
}
