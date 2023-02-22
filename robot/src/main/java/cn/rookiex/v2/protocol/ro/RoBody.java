package cn.rookiex.v2.protocol.ro;

import cn.rookiex.v2.protocol.ProtocolBody;

import java.nio.ByteBuffer;

/**
 * @author rookieX 2023/2/21
 */
public class RoBody implements ProtocolBody {

    ByteBuffer byteBuffer;

    public RoBody(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    /**
     * data 数据
     */
    public void getData(byte[] protobuf) {
        byteBuffer.position(RoProtocol.bodyIndex);
        byteBuffer.get(protobuf);
    }

    public void setData(byte[] protobuf) {
        byteBuffer.position(RoProtocol.bodyIndex);
        byteBuffer.put(protobuf);
    }
}
