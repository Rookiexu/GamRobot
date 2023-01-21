package cn.rookiex.sentinel.observer;

/**
 * 观察者接口
 *
 * @author rookieX 2022/12/10
 */
public interface Observer {

    void update(UpdateEvent message);
}
