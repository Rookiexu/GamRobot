package cn.rookiex.sentinel.observer.observed;

/**
 * @author rookieX 2022/12/27
 */
public interface MsgInfo {

    /**
     * @return 收到消息时间
     */
    long getCreateTime();

    /**
     * @param time 收到消息时间
     */
    void setCreateTime(long time);
}
