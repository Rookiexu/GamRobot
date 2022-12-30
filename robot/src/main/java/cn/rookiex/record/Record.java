package cn.rookiex.record;

import cn.rookiex.observer.*;
import cn.rookiex.observer.observed.ObservedEvents;
import cn.rookiex.observer.observed.ObservedParams;
import cn.rookiex.record.info.ProcessorInfo;
import cn.rookiex.record.window.RunWindow;
import cn.rookiex.record.window.Window;
import cn.rookiex.record.window.WindowRecord;
import cn.rookiex.record.window.WindowRecordImpl;
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
public class Record implements Observer, TickLog, WindowRecord {

    private final WindowRecordImpl windowRecord = new WindowRecordImpl();

    private Set<Integer> processorIds =  Sets.newHashSet();

    @Override
    public void initWindow(int windowSize, int windowWide){
        List<Window> windowList = Lists.newArrayListWithCapacity(windowSize);
        for (int i = 0; i < windowSize; i++) {
            RunWindow runWindow = new RunWindow();
            runWindow.setProcessorIds(processorIds);
            runWindow.init();
            windowList.add(runWindow);
        }

        windowRecord.initWindow(windowSize, windowWide, windowList);
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
    public RunWindow getCurWindows(){
        return (RunWindow) windowRecord.getCurWindows();
    }

    @Override
    public void update(UpdateEvent message) {
        RunWindow curWindows = getCurWindows();
        switch (message.getKey()){
            case ObservedEvents.INCR_COON:
                curWindows.incrProcessorInt(ProcessorInfo::getTotalCoon, (Integer) message.get(ObservedParams.PROCESSOR_ID));
                break;
            case ObservedEvents.INCR_SEND:
                curWindows.dealSend((Integer) message.get(ObservedParams.PROCESSOR_ID), message);
                curWindows.addActRobot((Integer) message.get(ObservedParams.PROCESSOR_ID), (String) message.get(ObservedParams.ROBOT_ID));
                break;
            case ObservedEvents.INCR_RESP:
                curWindows.incrProcessorLong(ProcessorInfo::getTotalResp, (Integer) message.get(ObservedParams.PROCESSOR_ID));
                curWindows.addActRobot((Integer) message.get(ObservedParams.PROCESSOR_ID), (String) message.get(ObservedParams.ROBOT_ID));
                break;
            case ObservedEvents.INCR_RESP_DEAL:
                curWindows.dealRespDeal((Integer) message.get(ObservedParams.PROCESSOR_ID), message);
                curWindows.addActRobot((Integer) message.get(ObservedParams.PROCESSOR_ID), (String) message.get(ObservedParams.ROBOT_ID));
                break;
            case ObservedEvents.INCR_ROBOT:
                curWindows.addActRobot((Integer) message.get(ObservedParams.PROCESSOR_ID), (String) message.get(ObservedParams.ROBOT_ID));
                break;
        }
    }

    @Override
    public void logInfo(){
        RunWindow curWindows = getCurWindows();
        StringBuilder builder = new StringBuilder();
        builder.append("\n").append("当前时间窗口(近").append(windowRecord.getWindowWide()/1000).append("秒)运行情况统计:")
                .append(curWindows.getLogInfo());


        RunWindow logRecord = new RunWindow();
        List<Window> runWindowList = windowRecord.getWindowList();
        for (Window record : runWindowList) {
            logRecord.sum((RunWindow) record);
        }
        builder.append("\n").append("全部时间窗口(近").append(windowRecord.getWindowWide()*windowRecord.getWindowSize()/1000d/60d).append("分钟)运行情况统计:")
                .append(logRecord.getLogInfo());

        log.info(builder);
    }
}
