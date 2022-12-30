package cn.rookiex.record.window;

import cn.rookiex.observer.UpdateEvent;
import cn.rookiex.observer.observed.ObservedParams;

/**
 * @author rookieX 2022/12/30
 */
public class SlowWindow implements Window {


    @Override
    public void init() {

    }

    @Override
    public void clear() {

    }

    @Override
    public String getLogInfo() {
        return null;
    }

    public void dealSlowResp(UpdateEvent message) {
        Object o = message.get(ObservedParams.RESP_DEAL_COST);

        //处理返回消息
        int waitId = (int) message.get(ObservedParams.WAIT_RESP_ID);
        //响应耗时记录
        long respCost = (long) message.get(ObservedParams.RESP_COST);


    }

    public void sum(SlowWindow record) {

    }
}
