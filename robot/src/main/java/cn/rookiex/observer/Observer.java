package cn.rookiex.observer;

import java.util.Map;

/**
 * 观察者接口
 *
 * @author rookieX 2022/12/10
 */
public interface Observer {

    void update(UpdateEvent message);
}
