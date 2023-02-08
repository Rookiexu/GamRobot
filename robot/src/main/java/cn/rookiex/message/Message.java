package cn.rookiex.message;


/**
 * @author rookieX 2022/12/8
 */
public interface Message {

    int getMsgId();

    void setMsgId(int id);

     byte[] getDataBytes();

     void setDataBytes(byte[] data);

     void setData(Object data);

    <T> T parseData(Class<T> dataClass);
}
