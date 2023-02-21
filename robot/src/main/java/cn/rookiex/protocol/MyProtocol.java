package cn.rookiex.protocol;

import java.nio.ByteBuffer;

/**
 * @author rookieX 2023/2/21
 */
public class MyProtocol implements IProtocol {

    /**
     * 协议index位置
     * */
    public static int versionIndex = 0;
    public static int encryptIndex = 1;
    public static int lengthIndex = 3;
    public static int cmdIndex = 9;
    public static int sessionIndex = 11;
    public static int bodyIndex = 19;

    protected ProtocolHead head;
    protected ProtocolBody body;
    protected ByteBuffer byteBuffer;

    protected MyProtocol(ByteBuffer byteBuffer) {
        body = new ProtocolBody(byteBuffer);
        head = new ProtocolHead(byteBuffer);
        this.byteBuffer = byteBuffer;
    }

    /**
     * @param buffer 完整消息内容
     * @return 协议对象
     * @throws RuntimeException
     */
    public static MyProtocol create(ByteBuffer buffer) throws RuntimeException {
        return new MyProtocol(buffer);
    }

    public static MyProtocol create(short command, long session, byte[] dataBuf) {
        return create(IProtocol.IS_ENCRYPT, IProtocol.VERSION, command, session, dataBuf);
    }

    public static MyProtocol create(boolean isEncrypt, short command, long session, byte[] dataBuf) {
        return create(isEncrypt, IProtocol.VERSION, command, session, dataBuf);
    }

    public static MyProtocol create(boolean isEncrypt, byte version, short command, long session, byte[] dataBuf) {
        int protobuf_length = dataBuf == null ? 0 : dataBuf.length;
        ByteBuffer protocolBuffer = ByteBuffer.allocate(ProtocolHead.headSize + protobuf_length);

        MyProtocol protocol = new MyProtocol(protocolBuffer);
        //设置封包头
        protocol.head.setEncrypt(isEncrypt);
        protocol.head.setVersion(version);
        protocol.head.setLength(dataBuf.length);
        protocol.head.setCmd(command);
        protocol.head.setSession(session);

        if (protobuf_length > 0) {
            protocol.body.setData(dataBuf);
        }
        protocolBuffer.rewind();
        return protocol;
    }


    @Override
    public ProtocolHead getHead() {
        return head;
    }

    @Override
    public ProtocolBody getBody() {
        return body;
    }
}
