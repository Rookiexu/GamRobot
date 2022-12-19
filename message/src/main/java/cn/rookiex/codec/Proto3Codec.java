package cn.rookiex.codec;

import com.google.protobuf.GeneratedMessageV3;

import java.lang.reflect.InvocationTargetException;


/**
 * @author rookieX 2022/12/19
 */
public class Proto3Codec implements DataCodec {
    @Override
    public byte[] encode(Object data) {
        return ((GeneratedMessageV3) data).toByteArray();
    }

    @Override
    public <T> T decode(byte[] data, Class<T> dataClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object invoke = dataClass.getMethod("parseFrom", byte[].class).invoke(null, (Object) data);
        return (T) invoke;
    }

    @Override
    public String getName() {
        return "proto3";
    }
}
