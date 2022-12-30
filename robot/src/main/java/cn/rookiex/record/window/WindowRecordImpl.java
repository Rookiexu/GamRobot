package cn.rookiex.record.window;

import lombok.Getter;

import java.util.List;

/**
 * @author rookieX 2022/12/30
 */
@Getter
public class WindowRecordImpl implements WindowRecord {

    private List<Window> windowList;

    private int windowIndex;

    private int windowWide;

    private int windowSize;

    private long windowsTime;

    @Override
    public void initWindow(int windowSize, int windowWide) {
        this.windowWide = windowWide;
        this.windowSize = windowSize;
    }

    @Override
    public void initWindow(int windowSize, int windowWide, List<Window> windowList) {
        this.windowWide = windowWide;
        this.windowSize = windowSize;
        this.windowList = windowList;
    }

    @Override
    public Window getCurWindows() {
        boolean isChange = false;
        long l = System.currentTimeMillis();
        if (windowsTime == 0){
            windowsTime = l;
            windowIndex = 0;
            isChange = true;
        }else {
            while (l - windowsTime > windowWide){
                windowIndex++;
                windowsTime += windowWide;
                isChange = true;
            }
        }
        Window runWindow = windowList.get(windowIndex % windowSize);
        if (isChange){
            runWindow.clear();
        }
        return runWindow;
    }
}
