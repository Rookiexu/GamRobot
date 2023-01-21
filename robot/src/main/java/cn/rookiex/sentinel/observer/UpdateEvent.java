package cn.rookiex.sentinel.observer;

import java.util.Map;

/**
 * @author rookieX 2022/12/19
 */
public interface UpdateEvent extends Map<String, Object> {

    String getKey();

    void setKey(String message);
}
