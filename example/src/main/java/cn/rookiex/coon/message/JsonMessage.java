package cn.rookiex.coon.message;

import cn.rookiex.message.Message;
import cn.rookiex.message.codec.DataCodec;
import cn.rookiex.message.codec.JsonCodec;
import cn.rookiex.sentinel.record.info.MsgInfo;
import com.alibaba.fastjson.JSONObject;

/**
 * @author rookieX 2023/2/9
 */
public class JsonMessage implements Message, MsgInfo {

    private int messageId;

    private long createTime;

    private byte[] data;

    public static DataCodec dataCodec = new JsonCodec();

    public JsonMessage(int message, JSONObject msgStr){
        this.messageId = message;
        this.data = dataCodec.encode(msgStr);
        this.createTime = System.currentTimeMillis();
    }

    public JsonMessage(){
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
    public short msgType() {
        return MessageConstants.JSON;
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
