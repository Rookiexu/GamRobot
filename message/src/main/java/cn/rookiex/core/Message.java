package cn.rookiex.core;


/**
 * @author rookieX 2022/12/8
 */
public interface Message {
    int messageId();

     <T> T getMessage(Class<T> clazz);
}
