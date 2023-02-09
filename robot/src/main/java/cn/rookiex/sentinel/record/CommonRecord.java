package cn.rookiex.sentinel.record;

import cn.rookiex.sentinel.pubsub.*;
import cn.rookiex.sentinel.pubsub.cons.SystemEventsKeys;
import cn.rookiex.sentinel.pubsub.cons.SystemEventParams;
import cn.rookiex.sentinel.record.info.ProcessorInfo;
import cn.rookiex.sentinel.record.window.impl.CommonWindow;
import cn.rookiex.sentinel.record.window.Window;
import cn.rookiex.sentinel.record.window.WindowsManager;
import cn.rookiex.sentinel.record.window.impl.WindowsManagerImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Set;

/**
 * @author rookieX 2022/12/9
 */
@Getter
@Log4j2
public class CommonRecord implements Subscriber, LogSubscriber, WindowsManager {

    private final WindowsManager windowsManager = new WindowsManagerImpl();

    private Set<Integer> processorIds =  Sets.newHashSet();

    @Override
    public void initWindow(int windowSize, int windowWide){
        List<Window> windowList = Lists.newArrayListWithCapacity(windowSize);
        for (int i = 0; i < windowSize; i++) {
            CommonWindow commonWindow = new CommonWindow();
            commonWindow.setProcessorIds(processorIds);
            commonWindow.init();
            windowList.add(commonWindow);
        }

        windowsManager.initWindow(windowSize, windowWide, windowList);
    }

    @Override
    public void initWindow(int windowSize, int windowWide, List<Window> windowList) {
        initWindow(windowSize, windowWide);
    }

    /**
     * 单线程,所以不需要锁
     * @return
     */
    @Override
    public CommonWindow getCurWindows(){
        return (CommonWindow) windowsManager.getCurWindows();
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

    @Override
    public void update(SystemEvent message) {
        CommonWindow curWindows = getCurWindows();
        switch (message.getKey()){
            case SystemEventsKeys.INCR_COON:
                curWindows.incrProcessorInt(ProcessorInfo::getTotalCoon, (Integer) message.get(SystemEventParams.PROCESSOR_ID));
                break;
            case SystemEventsKeys.INCR_SEND:
                curWindows.dealSend((Integer) message.get(SystemEventParams.PROCESSOR_ID), message);
                curWindows.addActRobot((Integer) message.get(SystemEventParams.PROCESSOR_ID), (String) message.get(SystemEventParams.ROBOT_ID));
                break;
            case SystemEventsKeys.INCR_RESP:
                curWindows.incrProcessorLong(ProcessorInfo::getTotalResp, (Integer) message.get(SystemEventParams.PROCESSOR_ID));
                curWindows.addActRobot((Integer) message.get(SystemEventParams.PROCESSOR_ID), (String) message.get(SystemEventParams.ROBOT_ID));
                break;
            case SystemEventsKeys.INCR_RESP_DEAL:
                curWindows.dealRespDeal((Integer) message.get(SystemEventParams.PROCESSOR_ID), message);
                curWindows.addActRobot((Integer) message.get(SystemEventParams.PROCESSOR_ID), (String) message.get(SystemEventParams.ROBOT_ID));
                break;
            case SystemEventsKeys.INCR_ROBOT:
                curWindows.addActRobot((Integer) message.get(SystemEventParams.PROCESSOR_ID), (String) message.get(SystemEventParams.ROBOT_ID));
                break;
        }
    }

    @Override
    public void logInfo(){
        CommonWindow curWindows = getCurWindows();
        StringBuilder builder = new StringBuilder();
        builder.append("\n").append("当前时间窗口(近").append(windowsManager.getWindowWide()/1000).append("秒)运行情况统计:")
                .append(curWindows.getLogInfo());


        CommonWindow logRecord = new CommonWindow();
        List<Window> runWindowList = windowsManager.getWindowList();
        for (Window record : runWindowList) {
            logRecord.sum((CommonWindow) record);
        }
        builder.append("\n").append("全部时间窗口(近").append(windowsManager.getWindowWide()* windowsManager.getWindowSize()/1000d/60d).append("分钟)运行情况统计:")
                .append(logRecord.getLogInfo());

        log.info(builder);
    }
}
