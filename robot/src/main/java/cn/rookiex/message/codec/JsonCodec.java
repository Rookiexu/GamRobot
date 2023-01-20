package cn.rookiex.message.codec;

import com.alibaba.fastjson.JSON;

/**
 * @author rookieX 2022/12/19
 */
public class JsonCodec implements DataCodec {
    @Override
    public byte[] encode(Object data) {
        return JSON.toJSONBytes(data);
    }

    @Override
    public <T> T decode(byte[] data, Class<T> dataClass) {
        return (T) JSON.parseObject(data, dataClass);
    }

    @Override
    public String getName() {
        return "JsonCodec";
    }
}
