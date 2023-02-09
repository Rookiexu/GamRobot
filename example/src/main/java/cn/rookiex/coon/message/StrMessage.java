package cn.rookiex.coon.message;


import cn.rookiex.message.codec.DataCodec;
import cn.rookiex.message.codec.StringCodec;
import cn.rookiex.message.Message;
import cn.rookiex.sentinel.observer.observed.MsgInfo;

/**
 * @author rookieX 2022/12/14
 */
public class StrMessage implements Message, MsgInfo {

    private int messageId;

    private long createTime;

    private byte[] data;

    public static DataCodec dataCodec = new StringCodec();

    public StrMessage(int message, String msgStr){
        this.messageId = message;
        this.data = dataCodec.encode(msgStr);
        this.createTime = System.currentTimeMillis();
    }

    public StrMessage(){
        this.createTime = System.currentTimeMillis();
    }

    @Override
    public int getMsgId() {
        return messageId;
    }

    @Override
    public void setMsgId(int id) {
        this.messageId = id;
    }

    @Override
    public byte[] getDataBytes() {
        return data;
    }

    @Override
    public void setDataBytes(byte[] data) {
        this.data = data;
    }

    @Override
    public void setData(Object data) {
        this.data = dataCodec.encode(data);
    }

    @Override
    public <T> T parseData(Class<T> dataClass) {
        return dataCodec.decode(data, dataClass);
    }

    @Override
    public long getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime(long time) {
        this.createTime = time;
    }
}
