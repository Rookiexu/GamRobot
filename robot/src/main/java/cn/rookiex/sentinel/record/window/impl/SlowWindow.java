package cn.rookiex.sentinel.record.window.impl;

import cn.rookiex.sentinel.pubsub.SystemEvent;
import cn.rookiex.sentinel.pubsub.cons.SystemEventParams;
import cn.rookiex.sentinel.record.info.RespondBucket;
import cn.rookiex.sentinel.record.window.Window;
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
        //todo 累计耗时最多消息
        //todo 单次耗时最多消息
        //todo 耗时消息总数,耗时排名
        //todo 耗时消息单次,单次排名
        return toString();
    }

    public void dealSlowResp(SystemEvent message) {
        //处理返回消息
        int waitId = (int) message.get(SystemEventParams.WAIT_RESP_ID);
        //响应耗时记录
        long respCost = (long) message.get(SystemEventParams.RESP_COST);

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
            mergeInteger(fromBucket.getCostBucket(), toBucket.getCostBucket());
        }
    }

    private void mergeInteger(Map<Integer, Integer> from, Map<Integer, Integer> to){
        for (Integer id : from.keySet()) {
            to.merge(id, from.get(id), Integer::sum);
        }
    }
}
