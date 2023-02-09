package cn.rookiex.sentinel.pubsub;

/**
 * 订阅中心,事件总线
 * @author rookieX 2022/12/10
 */
public interface EventBus {

    void subscribe(Subscriber o);

    void publish(SystemEvent message);
}
