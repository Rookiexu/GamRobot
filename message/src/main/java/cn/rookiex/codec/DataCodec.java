package cn.rookiex.codec;

import java.lang.reflect.InvocationTargetException;

/**
 * 消息编解码
 *
 * @author rookieX 2022/12/19
 */
public interface DataCodec {

    /**
     * 对象->字节数组
     * @param data 数据对象
     * @return 数据对象的字节数组
     */
    byte[] encode(Object data);

    /**
     * 字节数组->对象
     * @param data 数据对象
     * @param dataClass 要转的类
     * @param <T> t
     * @return 解码对象
     */
    <T> T decode(byte[] data, Class<T> dataClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    String getName();
}
