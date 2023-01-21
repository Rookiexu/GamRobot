package cn.rookiex.sentinel.record;

import cn.rookiex.sentinel.observer.Observer;
import cn.rookiex.sentinel.observer.UpdateEvent;
import cn.rookiex.sentinel.observer.observed.ObservedEvents;
import cn.rookiex.sentinel.observer.observed.ObservedParams;
import cn.rookiex.sentinel.record.window.*;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * @author rookieX 2022/12/30
 */
@Log4j2
public class SlowMsgRecord implements Observer, TickLog, WindowRecord {

    @Override
    public void update(UpdateEvent message) {
        SlowWindow slowWindow = getCurWindows();
        if (ObservedEvents.INCR_RESP_DEAL.equals(message.getKey())) {
            long costTime = (long) message.get(ObservedParams.RESP_COST);
            if (costTime > 200){
                slowWindow.dealSlowResp(message);
            }
        }
    }

    @Override
    public void logInfo() {
        SlowWindow curWindows = getCurWindows();
        StringBuilder builder = new StringBuilder();
        builder.append("\n").append("当前时间窗口(近").append(windowRecord.getWindowWide()/1000).append("秒)高延迟消息情况统计:")
                .append(curWindows.getLogInfo());


        SlowWindow logRecord = new SlowWindow();
        List<Window> runWindowList = windowRecord.getWindowList();
        for (Window record : runWindowList) {
            logRecord.sum((SlowWindow) record);
        }
        builder.append("\n").append("全部时间窗口(近").append(windowRecord.getWindowWide()*windowRecord.getWindowSize()/1000d/60d).append("分钟)高延迟消息情况统计:")
                .append(logRecord.getLogInfo());

         log.info(builder);
    }

    private final WindowRecordImpl windowRecord = new WindowRecordImpl();


    @Override
    public void initWindow(int windowSize, int windowWide){
        List<Window> windowList = Lists.newArrayListWithCapacity(windowSize);
        for (int i = 0; i < windowSize; i++) {
            SlowWindow runWindow = new SlowWindow();
            windowList.add(runWindow);
        }

        windowRecord.initWindow(windowSize, windowWide, windowList);
    }

    @Override
    public void initWindow(int windowSize, int windowWide, List<Window> windowList) {
        initWindow(windowSize, windowWide);
    }


    @Override
    public SlowWindow getCurWindows() {
        return (SlowWindow) windowRecord.getCurWindows();
    }
}
