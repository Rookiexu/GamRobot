package cn.rookiex.coon;

import cn.rookiex.core.Message;

/**
 * @author rookieX 2022/12/14
 */
public class SimpleMessage implements Message {

    private String message;
    private int messageId;

    public SimpleMessage(int messageId, String message) {
        this.message = message;
        this.messageId = messageId;
    }

    @Override
    public int getMsgId() {
        return messageId;
    }

    @Override
    public <T> T getData(Class<T> clazz) {
        return (T) message;
    }

    public String getData(){
        return message;
    }
}
