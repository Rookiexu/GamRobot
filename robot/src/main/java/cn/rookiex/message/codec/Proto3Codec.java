package cn.rookiex.message.codec;

import com.google.protobuf.GeneratedMessageV3;
import lombok.SneakyThrows;


/**
 * @author rookieX 2022/12/19
 */
public class Proto3Codec implements DataCodec {
    @Override
    public byte[] encode(Object data) {
        return ((GeneratedMessageV3) data).toByteArray();
    }

    @SneakyThrows
    @Override
    public <T> T decode(byte[] data, Class<T> dataClass){
        Object invoke = dataClass.getMethod("parseFrom", byte[].class).invoke(null, (Object) data);
        return (T) invoke;
    }

    @Override
    public String getName() {
        return "proto3";
    }
}
