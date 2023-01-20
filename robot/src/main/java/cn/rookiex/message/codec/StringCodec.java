package cn.rookiex.message.codec;


/**
 * @author rookieX 2022/12/19
 */
public class StringCodec implements DataCodec {
    @Override
    public byte[] encode(Object data){
        return ((String) data).getBytes();
    }

    @Override
    public <T> T decode(byte[] data, Class<T> dataClass){
        return (T) new String(data);
    }

    @Override
    public String getName() {
        return "String";
    }
}
