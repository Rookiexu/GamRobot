package cn.rookiex.manager;

import cn.rookiex.observer.ObservedEvents;
import cn.rookiex.observer.ObservedParams;
import cn.rookiex.observer.Observer;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author rookieX 2022/12/9
 */
@Getter
public class Record implements Observer {
    // processor  累计执行send,累计resp,周期执行,周期send
    // robot  机器人当前等待resp,累计执行send,累计resp
    // total  全服执行机器人数量

    private final AtomicInteger totalRobot = new AtomicInteger();

    private final AtomicInteger totalCoon = new AtomicInteger();

    private final AtomicInteger totalLogin = new AtomicInteger();

    private final AtomicLong totalSend = new AtomicLong();

    private final AtomicLong totalResp = new AtomicLong();

    private final AtomicLong totalDealResp = new AtomicLong();

    private final Map<Integer, AtomicLong> processorSend = Maps.newConcurrentMap();

    private final Map<Integer, AtomicLong> processorResp = Maps.newConcurrentMap();

    private final Map<Integer, AtomicLong> processorDealResp = Maps.newConcurrentMap();

    public AtomicLong getProcessorSend(int id) {
        return dealGet(id, processorSend);
    }

    public AtomicLong getProcessorResp(int id) {
        return dealGet(id, processorResp);
    }

    public AtomicLong getProcessorDealResp(int id) {
        return dealGet(id, processorDealResp);
    }

    public AtomicLong dealGet(int id, Map<Integer, AtomicLong> processorMap){
        AtomicLong atomicLong = new AtomicLong();
        AtomicLong put = processorMap.put(id, atomicLong);
        return put == null ? atomicLong : put;
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
        }
    }

    private void dealIncrRespDeal(Map<String, Object> info) {

    }

    private void dealIncrResp(Map<String, Object> info) {

    }

    private void dealIncrSend(Map<String, Object> info) {

    }

    private void dealIncrLogin(Map<String, Object> info) {

    }

    private void dealIncrCoon(Map<String, Object> info) {
        Integer id = (Integer) info.get(ObservedParams.PROCESSOR_ID);
        this.getTotalCoon().incrementAndGet();

    }
}
