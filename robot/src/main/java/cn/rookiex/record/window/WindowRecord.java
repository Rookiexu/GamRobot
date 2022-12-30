package cn.rookiex.record.window;

import cn.rookiex.record.window.Window;

import java.util.List;

/**
 * @author rookieX 2022/12/30
 */
public interface WindowRecord {

    void initWindow(int windowSize, int windowWide);

    void initWindow(int windowSize, int windowWide, List<Window> windowList);

    Window getCurWindows();
}
