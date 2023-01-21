package cn.rookiex.sentinel.record.window;

/**
 * @author rookieX 2022/12/30
 */
public interface Window {
    void init();

    void clear();

    String getLogInfo();
}
