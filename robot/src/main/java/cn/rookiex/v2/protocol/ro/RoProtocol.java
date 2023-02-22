package cn.rookiex.v2.protocol.ro;

import cn.rookiex.v2.protocol.IProtocol;
import cn.rookiex.v2.protocol.ProtocolBuilder;

import java.nio.ByteBuffer;

/**
 * @author rookieX 2023/2/21
 */
public class RoProtocol implements IProtocol, ProtocolBuilder {

    /**
     * 协议index位置
     * */
    public static int versionIndex = 0;
    public static int encryptIndex = 1;
    public static int lengthIndex = 3;
    public static int cmdIndex = 9;
    public static int sessionIndex = 11;
    public static int bodyIndex = 19;

    protected RoHead head;
    protected RoBody body;
    protected ByteBuffer byteBuffer;

    private static final RoProtocol builder = new RoProtocol(null);

    protected RoProtocol(ByteBuffer byteBuffer) {
        body = new RoBody(byteBuffer);
        head = new RoHead(byteBuffer);
        this.byteBuffer = byteBuffer;
    }

    public static RoProtocol getBuilder(){
        return builder;
    }

    /**
     * @param buffer 完整消息内容
     * @return 协议对象
     * @throws RuntimeException
     */
    @Override
    public RoProtocol create(ByteBuffer buffer) throws RuntimeException {
        return new RoProtocol(buffer);
    }

    @Override
    public RoProtocol create(short command, long session, byte[] dataBuf) {
        return create(IProtocol.IS_ENCRYPT, IProtocol.VERSION, command, session, dataBuf);
    }

    @Override
    public RoProtocol create(boolean isEncrypt, short command, long session, byte[] dataBuf) {
        return create(isEncrypt, IProtocol.VERSION, command, session, dataBuf);
    }

    @Override
    public RoProtocol create(boolean isEncrypt, byte version, short command, long session, byte[] dataBuf) {
        int protobuf_length = dataBuf == null ? 0 : dataBuf.length;
        ByteBuffer protocolBuffer = ByteBuffer.allocate(bodyIndex + protobuf_length);

        RoProtocol protocol = new RoProtocol(protocolBuffer);
        //设置封包头
        protocol.head.setEncrypt(isEncrypt);
        protocol.head.setVersion(version);
        protocol.head.setCmd(command);
        protocol.head.setSession(session);
        protocol.head.setLength(protobuf_length);

        if (protobuf_length > 0) {
            protocol.body.setData(dataBuf);
        }
        protocolBuffer.rewind();
        return protocol;
    }


    @Override
    public RoHead getHead() {
        return head;
    }

    @Override
    public RoBody getBody() {
        return body;
    }

    @Override
    public int getBodyIndex() {
        return bodyIndex;
    }

    public byte[] toArray() {
        return byteBuffer.array();
    }
}
