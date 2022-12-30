package cn.rookiex.record.window;

import cn.rookiex.observer.UpdateEvent;
import cn.rookiex.observer.observed.ObservedParams;
import cn.rookiex.record.info.RespondBucket;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * @author rookieX 2022/12/30
 */
@Getter
@ToString
public class SlowWindow implements Window {

    private final Map<Integer, RespondBucket> msgBucket = Maps.newConcurrentMap();

    @Override
    public void init() {

    }

    @Override
    public void clear() {

    }

    @Override
    public String getLogInfo() {
        return toString();
    }

    public void dealSlowResp(UpdateEvent message) {
        //处理返回消息
        int waitId = (int) message.get(ObservedParams.WAIT_RESP_ID);
        //响应耗时记录
        long respCost = (long) message.get(ObservedParams.RESP_COST);

        RespondBucket respondBucket = new RespondBucket();
        RespondBucket before = msgBucket.putIfAbsent(waitId, respondBucket);
        if (before != null){
            before.addCost((int) respCost);
        }else {
            respondBucket.addCost((int) respCost);
        }
    }

    public void sum(SlowWindow record) {
        Map<Integer, RespondBucket> from = record.getMsgBucket();
        Map<Integer, RespondBucket> to = getMsgBucket();

        for (Integer id : from.keySet()) {
            RespondBucket fromBucket = from.get(id);
            RespondBucket toBucket = to.get(id);
            if (toBucket == null){
                toBucket = new RespondBucket();
                to.put(id, toBucket);
            }
            mergeInteger(toBucket.getCostBucket(), fromBucket.getCostBucket());
        }
    }

    private void mergeInteger(Map<Integer, Integer> from, Map<Integer, Integer> to){
        for (Integer id : from.keySet()) {
            to.merge(id, from.get(id), Integer::sum);
        }
    }
}
