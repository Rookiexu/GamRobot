package cn.rookiex.v2.protocol;

import java.nio.ByteBuffer;

/**
 * @author rookieX 2023/2/22
 */
public interface ProtocolBuilder {
    IProtocol create(ByteBuffer buffer) throws RuntimeException;

    IProtocol create(short command, long session, byte[] dataBuf);

    IProtocol create(boolean isEncrypt, short command, long session, byte[] dataBuf);

    IProtocol create(boolean isEncrypt, byte version, short command, long session, byte[] dataBuf);
}
