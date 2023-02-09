package cn.rookiex.sentinel.pubsub;

/**
 * 订阅者
 *
 * @author rookieX 2022/12/10
 */
public interface Subscriber {

    void update(SystemEvent message);
}
