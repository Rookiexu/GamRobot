package cn.rookiex.observer;

import java.util.Map;

/**
 * @author rookieX 2022/12/19
 */
public interface UpdateEvent extends Map<String, Object> {

    String getMessage();

    void setMessage(String message);
}
