package cn.rookiex.core;


/**
 * @author rookieX 2022/12/8
 */
public interface Message {

    int getMsgId();

    void setMsgId(int id);

     byte[] getData();

     void setData(byte[] data);

     void setData(Object data);

    <T> T parseFrom(Class<T> dataClass);
}
