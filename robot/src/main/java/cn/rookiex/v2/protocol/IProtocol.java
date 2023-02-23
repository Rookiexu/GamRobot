package cn.rookiex.v2.protocol;


import java.nio.ByteBuffer;

/**
 * @author rookieX 2023/2/21
 */
public interface IProtocol {

    byte VERSION = 0x1;

    boolean IS_ENCRYPT = false;

    ProtocolHead getHead();

    ProtocolBody getBody();

    int getBodyIndex();

    ByteBuffer getByteBuffer();
}
