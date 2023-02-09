package cn.rookiex.message.codec;

//import com.google.protobuf.GeneratedMessageV3;
import lombok.SneakyThrows;


/**
 * todo 导入proto
 * @author rookieX 2022/12/19
 */
public class Proto3Codec implements DataCodec {
    @Override
    public byte[] encode(Object data) {
//        return ((GeneratedMessageV3) data).toByteArray();
        return null;
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
