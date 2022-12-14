package cn.rookiex.core;


/**
 * @author rookieX 2022/12/8
 */
public interface Message {
    int getMsgId();

     <T> T getData(Class<T> clazz);
}
