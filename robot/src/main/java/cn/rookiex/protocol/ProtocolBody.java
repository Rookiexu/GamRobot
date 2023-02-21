package cn.rookiex.protocol;

import java.nio.ByteBuffer;

/**
 * @author rookieX 2023/2/21
 */
public class ProtocolBody {

    ByteBuffer byteBuffer;

    public ProtocolBody(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    /**
     * data 数据
     */
    public void getData(byte[] protobuf) {
        byteBuffer.position(MyProtocol.bodyIndex);
        byteBuffer.get(protobuf);
    }

    public void setData(byte[] protobuf) {
        byteBuffer.position(MyProtocol.bodyIndex);
        byteBuffer.put(protobuf);
    }
}
