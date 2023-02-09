package cn.rookiex.sentinel.record;

import cn.rookiex.sentinel.pubsub.LogSubscriber;
import cn.rookiex.sentinel.pubsub.Subscriber;
import cn.rookiex.sentinel.pubsub.SystemEvent;
import cn.rookiex.sentinel.pubsub.cons.SystemEventsKeys;
import cn.rookiex.sentinel.pubsub.cons.SystemEventParams;
import cn.rookiex.sentinel.record.window.*;
import cn.rookiex.sentinel.record.window.impl.SlowWindow;
import cn.rookiex.sentinel.record.window.impl.WindowsManagerImpl;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * @author rookieX 2022/12/30
 */
@Log4j2
public class SlowMsgRecord implements Subscriber, LogSubscriber, WindowsManager {

    private final WindowsManager windowsManager = new WindowsManagerImpl();

    @Override
    public void update(SystemEvent message) {
        SlowWindow slowWindow = getCurWindows();
        if (SystemEventsKeys.INCR_RESP_DEAL.equals(message.getKey())) {
            long costTime = (long) message.get(SystemEventParams.RESP_COST);
            if (costTime > 200){
                slowWindow.dealSlowResp(message);
            }
        }
    }

    @Override
    public void logInfo() {
        SlowWindow curWindows = getCurWindows();
        StringBuilder builder = new StringBuilder();
        builder.append("\n").append("当前时间窗口(近").append(windowsManager.getWindowWide()/1000).append("秒)高延迟消息情况统计:")
                .append(curWindows.getLogInfo());


        SlowWindow logRecord = new SlowWindow();
        List<Window> runWindowList = windowsManager.getWindowList();
        for (Window record : runWindowList) {
            logRecord.sum((SlowWindow) record);
        }
        builder.append("\n").append("全部时间窗口(近").append(windowsManager.getWindowWide()* windowsManager.getWindowSize()/1000d/60d).append("分钟)高延迟消息情况统计:")
                .append(logRecord.getLogInfo());

         log.info(builder);
    }

    @Override
    public void initWindow(int windowSize, int windowWide){
        List<Window> windowList = Lists.newArrayListWithCapacity(windowSize);
        for (int i = 0; i < windowSize; i++) {
            SlowWindow runWindow = new SlowWindow();
            windowList.add(runWindow);
        }

        windowsManager.initWindow(windowSize, windowWide, windowList);
    }

    @Override
    public void initWindow(int windowSize, int windowWide, List<Window> windowList) {
        initWindow(windowSize, windowWide);
    }

    @Override
    public SlowWindow getCurWindows() {
        return (SlowWindow) windowsManager.getCurWindows();
    }

    @Override
    public int getWindowWide() {
        return windowsManager.getWindowWide();
    }

    @Override
    public List<Window> getWindowList() {
        return windowsManager.getWindowList();
    }

    @Override
    public int getWindowSize() {
        return windowsManager.getWindowSize();
    }
}
