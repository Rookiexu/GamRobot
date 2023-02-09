package cn.rookiex.sentinel.pubsub;

import java.util.Map;

/**
 * @author rookieX 2022/12/19
 */
public interface SystemEvent extends Map<String, Object> {

    String getKey();

    void setKey(String message);
}
