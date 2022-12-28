package cn.rookiex.record;

import cn.rookiex.observer.*;
import cn.rookiex.observer.observed.ObservedEvents;
import cn.rookiex.observer.observed.ObservedParams;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
public class Record implements Observer{

    private List<WindowRecord> windowList;

    private int windowIndex;

    private int windowWide;

    private int windowSize;

    private long windowsTime;

    private Set<Integer> processorIds =  Sets.newHashSet();

    public void initWindow(int windowSize, int windowWide){
        //60个 5秒
        windowList = Lists.newArrayListWithCapacity(windowSize);
        for (int i = 0; i < windowSize; i++) {
            WindowRecord windowRecord = new WindowRecord();
            windowRecord.init(processorIds);
            windowList.add(windowRecord);
        }

        this.windowWide = windowWide;
        this.windowSize = windowSize;
    }

    /**
     * 单线程,所以不需要锁
     * @return
     */
    public WindowRecord getCurWindows(){
        long l = System.currentTimeMillis();
        if (windowsTime == 0){
            windowsTime = l;
            windowIndex = 0;
        }else {
            while (l - windowsTime > windowWide){
                windowIndex++;
                windowsTime += windowWide;
            }
        }

        return windowList.get(windowIndex % windowSize);
    }



    @Override
    public void update(UpdateEvent message) {
        switch (message.getKey()){
            case ObservedEvents.INCR_COON:
                getCurWindows().incrProcessorInt(ProcessorRecord::getTotalCoon, (Integer) message.get(ObservedParams.PROCESSOR_ID));
                break;
            case ObservedEvents.INCR_SEND:
                getCurWindows().dealSend((Integer) message.get(ObservedParams.PROCESSOR_ID), message);
                break;
            case ObservedEvents.INCR_RESP:
                getCurWindows().incrProcessorLong(ProcessorRecord::getTotalResp, (Integer) message.get(ObservedParams.PROCESSOR_ID));
                break;
            case ObservedEvents.INCR_RESP_DEAL:
                getCurWindows().dealRespDeal((Integer) message.get(ObservedParams.PROCESSOR_ID), message);
                break;
            case ObservedEvents.INCR_ROBOT:
                getCurWindows().incrProcessorInt(ProcessorRecord::getTotalRobot, (Integer) message.get(ObservedParams.PROCESSOR_ID));
                break;
        }
    }


}
