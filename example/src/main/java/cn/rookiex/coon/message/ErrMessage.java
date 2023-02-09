package cn.rookiex.coon.message;

import cn.rookiex.message.Message;
import cn.rookiex.sentinel.observer.observed.MsgInfo;

/**
 * @author rookieX 2023/2/9
 */
public class ErrMessage implements Message, MsgInfo {

    private int messageId;

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
        return new byte[0];
    }

    @Override
    public void setDataBytes(byte[] data) {

    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public <T> T parseData(Class<T> dataClass) {
        return null;
    }

    @Override
    public short msgType() {
        return 0;
    }

    @Override
    public long getCreateTime() {
        return 0;
    }

    @Override
    public void setCreateTime(long time) {

    }
}
